# View 规范

## 介绍

View 层负责把 ViewModel 提供的状态渲染为 Compose UI，并把业务操作以回调形式交给上层。业务校验、状态更新、Repository 调用和页面跳转由 ViewModel 处理，View 不在点击 lambda 中堆叠业务逻辑。页面顶部栏的普通返回操作属于统一页面行为，Screen 可以直接调用 `navigateBack()`。每个页面固定使用 `Route → Screen → Content` 三层结构。

一个按钮事件的方向必须保持单向：

```text
用户点击 → Content 回调 → Screen 回调 → Route 绑定 ViewModel 方法
→ ViewModel 更新 StateFlow / 调用 Repository → Route 收到新状态 → Screen 重组
```

## 注释规范

View 文件同时包含 Route、Screen、Content 和 Preview，所有声明都需要能脱离上下文理解：

| 声明位置 | 注释要求 |
| --- | --- |
| Composable 参数 | 在函数 KDoc 中逐项使用 `@param` 说明状态、数据和事件回调 |
| Route 状态变量 | 每个 `collectAsState()` 上方单独添加中文行内注释，说明收集的具体状态 |
| Screen 与 Content 局部变量 | 在声明上方说明布局计算结果、展示数据或状态用途 |
| Preview 参数 | 在 Preview 函数 KDoc 中使用 `@param` 说明数据来源和预览用途 |

注释只描述页面结构、业务含义和设计原因，不记录修改过程。即使变量名较短或类型明确，也不能省略其用途说明。

## Route 层

Route 是 Graph 注册的页面入口，职责包括：

- 通过 `hiltViewModel()` 获取 ViewModel；
- 使用 `collectAsState()` 收集 `StateFlow`；
- 将状态和事件回调传给 Screen；
- 对需要监听页面返回结果的网络页调用一次 `observeRefreshState()`。

Route 不绘制具体布局，不直接访问 Repository。每个状态收集语句上方都应使用一条简短注释说明状态用途；事件尽量使用 `viewModel::method` 直接绑定。以下以分页页的状态为例展示标准 Route 写法：

文件位置：`feature/demo/view/NetworkListDemoScreen.kt`

```kotlin
package com.joker.kit.feature.demo.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.feature.demo.viewmodel.NetworkListDemoViewModel

/**
 * Network List Demo 路由
 *
 * @param viewModel Hilt 注入的 NetworkListDemoViewModel
 */
@Composable
internal fun NetworkListDemoRoute(
    viewModel: NetworkListDemoViewModel = hiltViewModel(),
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
        onRetry = viewModel::retryRequest,
    )
}
```

`collectAsState()` 是当前源码的实际策略；新增页面应先遵循项目现状，再根据生命周期需求评估 `collectAsStateWithLifecycle()` 的引入范围。注释应描述业务含义，例如“收集下拉刷新状态”，不要写成“获取数据”这类无法区分变量职责的注释。

Route 不应在 `LaunchedEffect` 中重复发起已经由 ViewModel `init` 触发的首屏请求，也不应在事件 lambda 中判断业务条件、更新状态或调用 Repository。只有当 Screen 回调签名与 ViewModel 方法不同时，Route 才做简单参数适配。

## Screen 层

Screen 接收可预览的状态和回调，按页面需要负责 `Scaffold`、页面骨架、状态分支和通用容器。下面的分页 Screen 在外层组合 `AppScaffold` 与 `BaseNetWorkListView`，Loading、Empty 和 Error 都在该层处理；只有成功分支进入 Content。顶级 `MainScreen` 因为承载多个子页面和底部导航，不使用 `Scaffold`，但仍保留 `MainContent`。

文件位置：`feature/demo/view/NetworkListDemoScreen.kt`

```kotlin
package com.joker.kit.feature.demo.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.joker.kit.core.base.state.BaseNetWorkListUiState
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.network.BaseNetWorkListView
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.feature.demo.skeleton.NetworkListLoadingSkeleton

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
        // Content 使用 MiuixPageLazyColumn 或 RefreshLayout 消费 Scaffold 内边距。
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

Screen 不使用 ViewModel，也不把成功数据的列表项直接写在状态容器内。顶部栏返回按钮可以直接调用 `navigateBack()`，不需要为此在 ViewModel 中增加包装方法。普通无网络页面没有 Loading、Empty 和 Error 分支时，Screen 仍然保留，负责外层结构并调用 Content。

## Content 层

Content 是每个页面必须保留的最终内容层。网络页面只在 Success 分支调用 Content；普通页面由 Screen 直接调用。Content 只接收可渲染数据和事件回调，不获取 ViewModel、不调用 Repository、不直接触发全局导航，也不处理 Loading、Empty 或 Error。

文件位置：`feature/demo/view/NetworkListDemoScreen.kt`

```kotlin
package com.joker.kit.feature.demo.view

import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.ui.component.refresh.RefreshLayout

/**
 * Network List Demo 内容
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
        // 逐项渲染商品业务数据
        itemsIndexed(list) { _, item ->
            GoodsListItem(goods = item)
        }
    }
}
```

Content 可以继续拆分私有 Composable；需要被同一 Feature 的多个页面复用时，再将子组件移入 `component/`。Content 本身仍作为 Screen 进入最终业务布局的统一入口。

## 预览规范

完整页面优先使用项目统一的 `@ScreenPreview` 和 `@ScreenPreviewDark`，局部组件使用 `@ComponentPreview*`。这些注解的设备组合与选择规则统一在[注解](../框架核心/annotation.md)中维护，Feature 只负责向 Screen 提供可渲染的数据。

简单页面可以在 Preview 中直接构造固定输入；复杂页面或同一页面的多种数据状态使用 `@PreviewParameter`。下面的 Provider 完整实现位于[数据层的预览数据示例](../框架核心/data.md#预览数据)，本页只展示页面如何消费：

```kotlin
package com.joker.kit.feature.demo.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.joker.kit.core.annotation.ScreenPreview
import com.joker.kit.core.annotation.ScreenPreviewDark
import com.joker.kit.core.base.state.BaseNetWorkListUiState
import com.joker.kit.core.data.preview.GoodsPreviewParameterProvider
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.model.entity.Goods

/**
 * 网络列表页面浅色预览
 *
 * @param goods 商品预览数据
 */
@ScreenPreview
@Composable
private fun NetworkListDemoScreenPreview(
    @PreviewParameter(GoodsPreviewParameterProvider::class)
    goods: List<Goods>,
) {
    AppTheme {
        NetworkListDemoScreen(
            // 空列表映射为空状态，其他输入映射为成功状态。
            uiState = if (goods.isEmpty()) {
                BaseNetWorkListUiState.Empty
            } else {
                BaseNetWorkListUiState.Success
            },
            list = goods,
            hasMoreData = goods.isNotEmpty(),
        )
    }
}

/**
 * 网络列表页面深色预览
 *
 * @param goods 商品预览数据
 */
@ScreenPreviewDark
@Composable
private fun NetworkListDemoScreenPreviewDark(
    @PreviewParameter(GoodsPreviewParameterProvider::class)
    goods: List<Goods>,
) {
    AppTheme(darkTheme = true) {
        NetworkListDemoScreen(
            // 空列表映射为空状态，其他输入映射为成功状态。
            uiState = if (goods.isEmpty()) {
                BaseNetWorkListUiState.Empty
            } else {
                BaseNetWorkListUiState.Success
            },
            list = goods,
            hasMoreData = goods.isNotEmpty(),
        )
    }
}
```

Preview 不能依赖真实 Hilt 容器或网络请求。单页数据可以紧贴页面，同一 Feature 的多个页面复用时放入 `feature/<domain>/data/`，面向 Core 共享模型或跨 Feature 复用时放入 `core/data/preview/`。Loading、Error 等不需要业务对象的状态，可以各自增加一个简短 Preview，不必强行塞进同一个 Provider。

## 官方文档

- [项目注解：Compose 预览](../框架核心/annotation.md)
- [项目数据层：预览数据](../框架核心/data.md#预览数据)
- [Jetpack Compose 状态](https://developer.android.com/develop/ui/compose/state)
- [Compose 预览工具](https://developer.android.com/develop/ui/compose/tooling)
- [Miuix 官方组件文档](https://compose-miuix-ui.github.io/miuix/zh_CN/components/)
