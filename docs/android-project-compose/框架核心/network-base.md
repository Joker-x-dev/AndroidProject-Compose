# 非分页网络基类

## 介绍

`BaseNetWorkViewModel<T>`、`BaseNetWorkUiState<T>` 与 `BaseNetWorkView` 组成项目的非分页请求方案。它负责把 `Flow<NetworkResponse<T>>` 转换为加载、成功和失败三种页面状态，适用于详情页或一次性数据请求；分页列表应使用[分页列表](./pagination.md)中的四态方案。

开始前应已完成[网络请求](./network.md)和[数据层](./data.md)的接入。本页不会直接声明 Retrofit 接口，而是消费 Repository 已暴露的 Flow。

## 模块结构

以下目录展示非分页网络状态从 ViewModel 到通用 View 的实现位置。

```text
core/
├── base/
│   ├── state/BaseNetWorkUiState.kt
│   └── viewmodel/BaseNetWorkViewModel.kt
├── result/
│   ├── ResultExt.kt
│   └── ResultHandler.kt
└── ui/component/network/BaseNetWorkView.kt
```

## 运行链路

一次请求按以下顺序转换为页面状态。

```text
Route 触发 executeRequest()
  → requestApiFlow() 返回 NetworkResponse Flow
  → asResult() 发出 Loading / Success / Error
  → ResultHandler 校验 NetworkResponse.code
  → BaseNetWorkUiState 驱动 BaseNetWorkView
```

业务成功统一由 `NetworkResponse.isSucceeded` 判断。当前示例项目的实现是 `code == 1000`，接入其他后端时应按实际响应协议修改这一集中判断，页面和 ViewModel 不应直接比较某个固定成功码。

当前响应模型的 `data` 类型为 `T?`。当 `isSucceeded` 为 `true` 但 `data` 为 `null` 时，`ResultHandler` 不会调用 `onData`，页面会继续保持请求开始时的状态。如果业务响应没有 `code/data/message` 包装，或者数据字段的类型与当前模型不同，应先调整 `NetworkResponse` 和结果处理层，再使用本基类。

这套三态没有独立 Empty 状态。详情接口允许返回“没有内容”时，应让业务模型明确表达空内容，或在成功内容中渲染空态；列表接口不要强行套用该基类。

## 页面状态

| 状态 | 数据 | `BaseNetWorkView` 默认内容 |
| --- | --- | --- |
| `Loading` | 无 | `PageLoading()` |
| `Success<T>` | `data: T` | 调用 `content(data)` |
| `Error` | `message: String?`、`exception: Throwable?` | `EmptyNetwork(onRetryClick = onRetry)` |

状态切换使用 `AnimatedContent`，默认淡入淡出动画时长为 300 ms。`customLoading` 与 `customError` 可替换默认占位组件。

## 核心 API

### `BaseNetWorkViewModel<T>`

| 名称 | 签名或类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `uiState` | `StateFlow<BaseNetWorkUiState<T>>` | `Loading` | 页面只读状态流 |
| `showErrorToast` | `protected open val Boolean` | `false` | 错误时是否调用默认 Toast |
| `enableMinLoadingTime` | `protected open val Boolean` | `false` | 是否保持至少 320 ms 的首屏加载态 |
| `requestApiFlow()` | `Flow<NetworkResponse<T>>` | 无 | 子类必须实现的请求入口 |
| `executeRequest()` | `Unit` | 无 | 发起请求并分发状态 |
| `retryRequest()` | `Unit` | 无 | 先写入 `Loading`，再重新请求 |
| `getSuccessData()` | `T` | 无 | 返回成功数据；非成功状态抛出 `IllegalStateException` |
| `observeRefreshState(key)` | `Unit` | `RefreshResultKey` | 监听一次导航刷新结果，`refresh == true` 时重新请求 |

### `BaseNetWorkView`

| 参数 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `uiState` | `BaseNetWorkUiState<T>` | 必传 | 当前页面状态 |
| `modifier` | `Modifier` | `Modifier` | 外层 `Box` 修饰符 |
| `padding` | `PaddingValues` | `PaddingValues()` | 通常接收 Scaffold 内边距 |
| `onRetry` | `() -> Unit` | `{}` | 默认错误页的重试回调 |
| `customLoading` | `@Composable (() -> Unit)?` | `null` | 自定义加载内容 |
| `customError` | `@Composable (() -> Unit)?` | `null` | 自定义错误内容 |
| `content` | `@Composable (T) -> Unit` | 必传 | 成功内容 |

## ViewModel 示例

文件位置：`feature/demo/viewmodel/NetworkDemoViewModel.kt`

下面是当前 `NetworkDemoViewModel` 的完整核心实现。`getGoodsInfo(id)` 返回 `Flow<NetworkResponse<Goods>>`，因此可以直接交给基类处理。

```kotlin
package com.joker.kit.feature.demo.viewmodel

import com.joker.kit.core.base.viewmodel.BaseNetWorkViewModel
import com.joker.kit.core.data.repository.GoodsRepository
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.model.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 网络状态 Demo ViewModel
 *
 * @param goodsRepository 商品仓库
 */
@HiltViewModel
class NetworkDemoViewModel @Inject constructor(
    private val goodsRepository: GoodsRepository
) : BaseNetWorkViewModel<Goods>() {

    /**
     * 获取商品信息数据流
     *
     * @return 商品详情响应流
     */
    override fun requestApiFlow(): Flow<NetworkResponse<Goods>> {
        return goodsRepository.getGoodsInfo("1")
    }

    init {
        // 子类初始化完成后发起首屏请求
        executeRequest()
    }
}
```

请求完成后，`uiState` 会进入 `Success` 或 `Error`。如果需要减少快速响应造成的加载骨架闪烁，可重写 `enableMinLoadingTime` 为 `true`。

## View 示例

文件位置：`feature/demo/view/NetworkDemoScreen.kt`

Route 负责收集状态和绑定事件，Screen 只接收状态与回调。下面省略商品字段的详细排版。

```kotlin
package com.joker.kit.feature.demo.view

import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.base.state.BaseNetWorkUiState
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.ui.component.network.BaseNetWorkView
import com.joker.kit.feature.demo.viewmodel.NetworkDemoViewModel

/**
 * Network Demo 路由
 *
 * @param viewModel Network Demo ViewModel
 */
@Composable
internal fun NetworkDemoRoute(
    viewModel: NetworkDemoViewModel = hiltViewModel()
) {
    // 收集页面请求状态
    val uiState by viewModel.uiState.collectAsState()

    NetworkDemoScreen(
        uiState = uiState,
        onRetry = viewModel::retryRequest
    )
}

/**
 * Network Demo 界面
 *
 * @param uiState 商品详情请求状态
 * @param onRetry 重试回调
 */
@Composable
internal fun NetworkDemoScreen(
    uiState: BaseNetWorkUiState<Goods>,
    onRetry: () -> Unit
) {
    BaseNetWorkView(
        uiState = uiState,
        onRetry = onRetry
    ) { goods ->
        NetworkDemoContent(data = goods)
    }
}

/**
 * Network Demo 成功态内容
 *
 * @param data 商品详情
 */
@Composable
private fun NetworkDemoContent(data: Goods) {
    Text(text = data.title)
}
```

错误页点击重试后会先回到 `Loading`，随后执行同一个 `requestApiFlow()`。`BaseNetWorkView` 在 Screen 中处理 Loading、Error 和 Success 分支，只有 Success 数据进入 `NetworkDemoContent`。

## 自定义状态内容

`customLoading` 和 `customError` 只替换视图，不改变 ViewModel 状态。以下 Screen 内片段把默认断网页替换为项目现有的通用错误页：

```kotlin
BaseNetWorkView(
    uiState = uiState,
    onRetry = onRetry,
    customError = {
        // 通用业务错误页继续复用同一个重试回调
        EmptyError(onRetryClick = onRetry)
    }
) { goods ->
    NetworkDemoContent(data = goods)
}
```

该片段需要导入 `com.joker.kit.core.ui.component.empty.EmptyError`。`customError` 当前不接收 `BaseNetWorkUiState.Error` 参数；如果页面必须显示服务端错误文案，应在 Screen 中先从 `uiState` 读取 `message`，再传给 Feature 自己定义的错误 Composable。

## 扩展与注意事项

- 重写 `onRequestStart`、`onRequestSuccess` 或 `onRequestError` 时，如仍需默认状态切换，应调用 `super` 或显式写入对应状态。
- `executeRequest()` 不会保存返回的 `Job`，连续调用可能产生并行请求；需要防重复提交的页面应在业务层增加互斥状态。
- `observeRefreshState()` 内部用 `Job` 保证同一 ViewModel 只注册一次监听，并随 `viewModelScope` 取消。
- `getSuccessData()` 只适合已确认处于成功态的同步读取，常规 UI 应订阅 `uiState`。

## 下一步

非分页页面到此形成完整闭环。需要下拉刷新和加载下一页时，继续阅读[分页列表](./pagination.md)；不要在三态基类上自行叠加页码状态。

## 官方文档

- [StateFlow 与 SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- [Compose 中的状态](https://developer.android.com/develop/ui/compose/state)
- [Kotlin Flow 异常处理](https://kotlinlang.org/docs/flow.html#flow-exceptions)
