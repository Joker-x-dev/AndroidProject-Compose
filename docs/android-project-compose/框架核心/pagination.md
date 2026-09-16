# 分页列表

## 介绍

项目的分页列表由 `BaseNetWorkListViewModel<T>` 管理页码和状态，由 `BaseNetWorkListView` 处理首屏四态，再由 `RefreshLayout` 统一封装下拉刷新、上拉加载和列表排版。该实现要求接口返回 `NetworkPageData<T>`；如果接口没有 `list` 与 `pagination` 结构，应在 Repository 或数据模型层完成转换。

本页是 Core 数据链路的最终组合页。开始前应已理解[请求结果处理](./result.md)、[网络请求](./network.md)、[数据层](./data.md)和 [UI 组件](./ui.md)。分页 ViewModel 只消费 Repository Flow，不在 Compose 中管理页码。

## 模块结构

以下目录展示分页状态、交互组件和 Demo 的对应位置。

```text
core/base/
├── state/BaseNetWorkListUiState.kt
└── viewmodel/BaseNetWorkListViewModel.kt
core/ui/component/
├── network/BaseNetWorkListView.kt
└── refresh/
    ├── RefreshLayout.kt
    └── RefreshContent.kt
feature/demo/
├── viewmodel/NetworkListDemoViewModel.kt
└── view/NetworkListDemoScreen.kt
```

## 状态与请求链路

页面从状态收集到下一页合并按以下链路执行。

```text
Route 收集 uiState/listData/刷新 Flow
  → BaseNetWorkListView 切换 Loading / Empty / Error / Success
  → Success 内容交给 RefreshLayout
  → Miuix PullToRefresh 触发 onRefresh，列表接近末尾时触发 onLoadMore
  → ViewModel 请求当前页并合并列表
```

### 页面状态

`BaseNetWorkListUiState` 是无参数的 sealed class：

| 状态 | 进入条件 | 默认内容 |
| --- | --- | --- |
| `Loading` | 首次请求或 `retryRequest()` | `PageLoading()` |
| `Success` | 第 1 页返回非空列表 | 成功内容 |
| `Empty` | 第 1 页返回空列表 | `EmptyData(onRetryClick = onRetry)` |
| `Error` | 第 1 页失败且当前没有旧数据 | `EmptyNetwork(onRetryClick = onRetry)` |

第 1 页刷新失败但已有旧列表时，状态保持 `Success`，同时结束刷新；加载更多失败时不会覆盖首屏状态，并会将 `currentPage` 回退一页。

### StateFlow

| Flow | 类型 | 初始值 | 用途 |
| --- | --- | --- | --- |
| `uiState` | `StateFlow<BaseNetWorkListUiState>` | `Loading` | 首屏四态 |
| `listData` | `StateFlow<List<T>>` | `emptyList()` | 已合并的列表数据 |
| `isRefreshing` | `StateFlow<Boolean>` | `false` | 头部刷新指示器 |
| `isLoadingMore` | `StateFlow<Boolean>` | `false` | 底部加载指示器 |
| `hasMoreData` | `StateFlow<Boolean>` | `false` | 是否显示加载更多 |

## 页码规则

`BaseNetWorkListViewModel` 当前默认 `currentPage = 1`、`pageSize = 10`。子类可重写 `pageSize`，但不能直接从 UI 修改受保护的 `currentPage`。

成功响应中的 `pagination` 用以下公式判断下一页：

```text
pagination.size * pagination.page < pagination.total
```

缺少 `pagination` 时，`hasMoreData` 会被设为 `false`。公式使用服务端返回的 `size` 与 `page`；字段为空时分别回退到基类的 `pageSize` 与当前请求页。

## ViewModel 示例

文件位置：`feature/demo/viewmodel/NetworkListDemoViewModel.kt`

以下按页面规范展示分页 ViewModel：首屏最短加载时间打开，接口页大小重写为 15，Repository 返回 `Flow<NetworkResponse<NetworkPageData<Goods>>>`。

```kotlin
package com.joker.kit.feature.demo.viewmodel

import com.joker.kit.core.base.viewmodel.BaseNetWorkListViewModel
import com.joker.kit.core.data.repository.GoodsRepository
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.model.network.NetworkPageData
import com.joker.kit.core.model.network.NetworkResponse
import com.joker.kit.core.model.request.GoodsSearchRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Network List Demo ViewModel
 *
 * @param goodsRepository 商品仓库
 */
@HiltViewModel
class NetworkListDemoViewModel @Inject constructor(
    private val goodsRepository: GoodsRepository
) : BaseNetWorkListViewModel<Goods>() {

    /** 首屏加载至少保持 320 ms，避免骨架瞬时消失 */
    override val enableMinLoadingTime: Boolean get() = true

    /** Demo 接口每页请求 15 条 */
    override val pageSize: Int get() = 15

    init {
        // ViewModel 初始化后只启动一次首屏请求
        initLoad()
    }

    /**
     * 请求当前页商品
     *
     * @return 商品分页响应流
     */
    override fun requestListData(): Flow<NetworkResponse<NetworkPageData<Goods>>> {
        return goodsRepository.getGoodsPage(
            GoodsSearchRequest(
                page = currentPage,
                size = pageSize
            )
        )
    }
}
```

`initLoad()`、`currentPage` 和 `requestListData()` 都位于 ViewModel 层；不要在 Compose 中拼接页码或直接调用 Retrofit。

## View 示例

文件位置：`feature/demo/view/NetworkListDemoScreen.kt`

Route 收集五个状态流，Screen 接收状态和事件，Content 将 Scaffold 的 `contentPadding` 交给 `RefreshLayout`。列表状态和顶部栏滚动行为都由刷新布局默认创建或从 `AppScaffold` 自动复用；列表页面间距由内部的 `MiuixPageLazyColumn` 统一叠加，调用方不再手写四方向 `PaddingValues`。

```kotlin
package com.joker.kit.feature.demo.view

import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.base.state.BaseNetWorkListUiState
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.network.BaseNetWorkListView
import com.joker.kit.core.ui.component.refresh.RefreshLayout
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.feature.demo.skeleton.NetworkListLoadingSkeleton
import com.joker.kit.feature.demo.viewmodel.NetworkListDemoViewModel

/**
 * Network List Demo 路由
 *
 * @param viewModel 分页列表 ViewModel
 */
@Composable
internal fun NetworkListDemoRoute(
    viewModel: NetworkListDemoViewModel = hiltViewModel()
) {
    // 收集页面状态
    val uiState by viewModel.uiState.collectAsState()
    // 收集列表数据
    val listData by viewModel.listData.collectAsState()
    // 收集下拉刷新状态
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    // 收集上拉加载状态
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    // 收集是否还有更多数据
    val hasMoreData by viewModel.hasMoreData.collectAsState()

    NetworkListDemoScreen(
        uiState = uiState,
        list = listData,
        isRefreshing = isRefreshing,
        isLoadingMore = isLoadingMore,
        hasMoreData = hasMoreData,
        onRefresh = viewModel::onRefresh,
        onLoadMore = viewModel::onLoadMore,
        onRetry = viewModel::retryRequest
    )
}
```

Route 只收集状态并传递事件。下面的 Screen 负责页面骨架和四态切换，仍属于同一个 `NetworkListDemoScreen.kt` 文件，沿用上一个代码块中的 package 与 imports。

```kotlin
/**
 * Network List Demo 界面
 *
 * @param uiState 列表页状态
 * @param list 商品列表数据
 * @param isRefreshing 是否正在刷新
 * @param isLoadingMore 是否正在加载更多
 * @param hasMoreData 是否还有更多数据
 * @param onRefresh 刷新回调
 * @param onLoadMore 加载更多回调
 * @param onRetry 重试回调
 */
@Composable
internal fun NetworkListDemoScreen(
    uiState: BaseNetWorkListUiState = BaseNetWorkListUiState.Loading,
    list: List<Goods> = emptyList(),
    isRefreshing: Boolean = false,
    isLoadingMore: Boolean = false,
    hasMoreData: Boolean = false,
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    onRetry: () -> Unit = {},
) {
    AppScaffold(
        titleText = "Network List Demo",
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
        nestedScrollInScaffold = false,
    ) { paddingValues ->
        // BaseNetWorkListView 只处理 Loading、Empty、Error 和 Success 状态切换。
        BaseNetWorkListView(
            uiState = uiState,
            onRetry = onRetry,
            customLoading = {
                NetworkListLoadingSkeleton(contentPadding = paddingValues)
            },
        ) {
            NetworkListDemoContent(
                contentPadding = paddingValues,
                list = list,
                isRefreshing = isRefreshing,
                isLoadingMore = isLoadingMore,
                hasMoreData = hasMoreData,
                onRefresh = onRefresh,
                onLoadMore = onLoadMore,
            )
        }
    }
}
```

### 成功态内容

`BaseNetWorkListView` 成功时才组合 `RefreshLayout`。列表内容使用 `LazyListScope` 的 `itemsIndexed`，与当前 Demo 一致。

```kotlin
/**
 * Network List Demo 成功态内容
 *
 * @param contentPadding Scaffold 提供的安全区内边距
 * @param list 商品列表数据
 * @param isRefreshing 是否正在刷新
 * @param isLoadingMore 是否正在加载更多
 * @param hasMoreData 是否还有更多数据
 * @param onRefresh 刷新回调
 * @param onLoadMore 加载更多回调
 */
@Composable
private fun NetworkListDemoContent(
    contentPadding: PaddingValues,
    list: List<Goods>,
    isRefreshing: Boolean,
    isLoadingMore: Boolean,
    hasMoreData: Boolean,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
) {
    RefreshLayout(
        list = list,
        isRefreshing = isRefreshing,
        isLoadingMore = isLoadingMore,
        hasMoreData = hasMoreData,
        contentPadding = contentPadding,
        onRefresh = onRefresh,
        onLoadMore = onLoadMore,
    ) {
        itemsIndexed(list) { _, goods ->
            // 列表项只负责展示单条商品
            GoodsListItem(goods = goods)
        }
    }
}
```

## `RefreshLayout` 行为

| 参数或机制 | 当前实现 |
| --- | --- |
| 手势容器 | Miuix `PullToRefresh`，由 `rememberPullToRefreshState()` 管理手势状态 |
| 列表模式 | 默认 `MiuixPageLazyColumn`，由 `RefreshContent` 通过 `content` 构建 |
| 网格模式 | `isGrid = true`，由 `RefreshContent` 通过 `gridContent` 构建 `LazyVerticalStaggeredGrid` |
| 加载更多触发 | 列表或网格滚动接近末尾 3 个条目时触发，并由 `hasMoreData`、`isLoadingMore` 和列表长度去重 |
| 头部文案 | 使用 `R.string.refresh_*` 资源传递给 Miuix `PullToRefresh` |
| 底部状态 | `isLoadingMore` 显示 `InfiniteProgressIndicator`；无更多且列表非空时显示无更多文案 |
| 滚动触感 | `RefreshContent` 根据 `LocalAppSettings.enableScrollEndHaptic` 控制边缘触感 |
| 顶部栏联动 | 传入 `scrollBehavior` 后交给 Miuix `PullToRefresh.topAppBarScrollBehavior` |

`RefreshContent` 的普通列表复用 `MiuixPageLazyColumn`，由 `contentPadding` 接收 Scaffold 安全区，由 `pagePadding` 统一提供水平和垂直页面间距；调用方只需传递 Scaffold 的 `contentPadding`。列表状态默认由 `RefreshLayout` 持有，顶部栏滚动行为优先复用 `AppScaffold` 提供的实例，脱离页面骨架时自动创建。列表与瀑布流切换仍使用 300 ms 淡入缩放动画。刷新组件不需要额外的 Application 初始化；刷新文案、手势状态和顶部栏滚动行为都在 `RefreshLayout` 内完成。

## 刷新、加载更多与重试

| 调用 | 前置条件 | 结果 |
| --- | --- | --- |
| `onRefresh()` | 加载更多未进行 | `currentPage = 1`，设置 `isRefreshing = true` 并重新请求 |
| `onLoadMore()` | 非加载中、有更多且列表非空 | 页码加一，设置 `isLoadingMore = true` 并追加数据 |
| `retryRequest()` | 任意错误态 | 清空刷新标记和更多标记，从第 1 页重新加载 |
| `observeRefreshState()` | 需要响应导航结果 | 监听 `RefreshResult.refresh == true` 后调用 `onRefresh()` |

首屏最短加载时间由 `enableMinLoadingTime` 控制，时长为 320 ms；刷新和加载更多成功态至少保持 500 ms，避免指示器闪现。加载更多失败会回退 `currentPage`，因此下一次触发仍请求失败页。

### 三种请求如何写入列表

| 请求类型 | 请求前 | 成功后 | 失败后 |
| --- | --- | --- | --- |
| 首次加载 | 第 1 页、列表为空、`Loading` | 替换列表，进入 `Success` 或 `Empty` | 列表为空时进入 `Error` |
| 下拉刷新 | 页码重置为 1，保留旧列表，`isRefreshing = true` | 用新列表替换旧列表 | 保留旧列表并回到 `Success`，但 `hasMoreData = false` |
| 加载更多 | 页码加 1，`isLoadingMore = true` | 追加新列表并更新 `hasMoreData` | 页码减 1，保留已加载列表 |

分页基类不会按 ID 去重。服务端分页数据可能重复或排序变化时，应在 Repository 或重写 `handleSuccess` 的业务实现中明确合并策略；不要在 Composable 每次重组时临时去重。

## 注意事项

- `pagination` 为空或总条数不满足公式时，底部会显示无更多内容，不会继续触发下一页。
- `onRefresh()` 在加载更多进行中会直接返回，避免两个请求同时修改列表。
- `onRefresh()` 没有额外阻止第二次刷新，基类也不保存和取消请求 `Job`；自定义手势或按钮触发时应避免重复调用。
- 首屏错误但已有旧列表时显示旧内容，用户可再次下拉刷新；首屏没有数据时才显示错误占位。
- `isGrid` 模式需要通过 `gridContent` 提供 `LazyStaggeredGridScope` 内容，不能把 `itemsIndexed` 的列表作用域直接传入。
- `pagination.size`、`page`、`total` 决定是否还有下一页；接口缺少这些元数据时基类会停止加载更多。

## 官方文档

- [Compose LazyColumn](https://developer.android.com/develop/ui/compose/lists)
- [Compose LazyVerticalStaggeredGrid](https://developer.android.com/develop/ui/compose/lists#lazy-staggered-grid)
- [Kotlin StateFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/)
- [Miuix 官方 PullToRefresh 文档](https://compose-miuix-ui.github.io/miuix/zh_CN/components/pulltorefresh)
