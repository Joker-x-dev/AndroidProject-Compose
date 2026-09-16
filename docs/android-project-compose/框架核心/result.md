# 请求结果处理

## 介绍

`core/result` 位于 Repository 与 ViewModel 状态之间：它先把 Flow 的“开始、发出数据、抛出异常”转换为 `Result`，再检查后端业务码并调用对应回调。它不负责发请求，也不负责 Compose 布局；网络线程由 Repository 决定，页面状态由 ViewModel 更新。

初学者需要先区分两类“成功”：HTTP 请求和 JSON 解析没有抛异常，只代表 Flow 能发出响应对象；业务是否成功，应由项目根据后端协议集中判断。当前示例项目通过 `NetworkResponse.isSucceeded` 判断，其实现为 `code == 1000`。

::: info 适配自己的响应协议
`code`、`message`、`data` 及成功码 `1000` 都是当前示例项目的响应契约，不是框架要求。其他项目可能使用字符串状态码、`success: Boolean`、不同的数据字段，甚至没有统一响应包装。接入时应集中调整响应模型、业务成功属性和 `ResultHandler`，再验证网络基类；不要在每个 ViewModel 中复制各自的成功判断。
:::

## 模块结构

以下三个文件分别定义状态、Flow 转换和回调分发。

```text
core/result/
├── Result.kt
├── ResultExt.kt
└── ResultHandler.kt
```

## `Result<T>`

| 类型 | 数据 | 产生时机 |
| --- | --- | --- |
| `Result.Loading` | 无 | `asResult()` 收集开始前 |
| `Result.Success<T>` | `data: T` | 上游 Flow 发出值 |
| `Result.Error` | `exception: Throwable` | 上游抛出异常，或收集过程失败 |

`asResult()` 的实现顺序是 `onStart { Loading } → map { Success } → catch { Error }`。它只能捕获 Flow 上游异常；响应是否满足项目业务成功规则仍由 `ResultHandler` 检查。

## `ResultHandler` 核心 API

### `handleResult`

文件位置：`core/result/ResultHandler.kt`。完整入口签名如下。

```kotlin
/**
 * 处理完整网络响应与生命周期回调
 *
 * @param scope 请求协程作用域
 * @param flow 网络响应结果流
 * @param showToast 是否显示默认错误提示
 * @param onLoading 请求开始回调
 * @param onSuccess 业务成功响应回调
 * @param onSuccessWithData 业务成功且数据非空回调
 * @param onError 业务失败或异常回调
 * @param onFinally 请求结束回调
 */
fun <T> handleResult(
    scope: CoroutineScope,
    flow: Flow<Result<NetworkResponse<T>>>,
    showToast: Boolean = true,
    onLoading: () -> Unit = {},
    onSuccess: (NetworkResponse<T>) -> Unit = {},
    onSuccessWithData: (T) -> Unit = {},
    onError: (String, Throwable?) -> Unit = { _, _ -> },
    onFinally: () -> Unit = {}
)
```

`onSuccess` 会先收到完整 `NetworkResponse`。当 `response.isSucceeded` 为 `true` 且 `data` 非空时，才继续调用 `onSuccessWithData`；成功响应的 `data == null` 会直接结束成功分支，不会调用 `onError`。只有响应不满足项目业务成功规则时，才按业务消息进入 `onError`。当前项目把 `code == 1000` 定义为成功。

### `handleResultWithData`

文件位置：`core/result/ResultHandler.kt`。只关心非空数据时使用以下入口。

```kotlin
/**
 * 处理成功且非空的业务数据
 *
 * @param scope 请求协程作用域
 * @param flow 网络响应结果流
 * @param showToast 是否显示默认错误提示
 * @param onLoading 请求开始回调
 * @param onData 业务成功且数据非空回调
 * @param onError 业务失败或异常回调
 * @param onFinally 请求结束回调
 */
fun <T> handleResultWithData(
    scope: CoroutineScope,
    flow: Flow<Result<NetworkResponse<T>>>,
    showToast: Boolean = true,
    onLoading: () -> Unit = {},
    onData: (T) -> Unit,
    onError: (String, Throwable?) -> Unit = { _, _ -> },
    onFinally: () -> Unit = {}
)
```

它是 `handleResult` 的简化包装，只暴露成功且有数据的回调。

## 与网络基类的连接

文件位置：`core/base/viewmodel/BaseNetWorkViewModel.kt`。下面只保留基类调用 `ResultHandler` 的相关片段，用来说明本页能力怎样把请求结果交给状态回调；完整页面 ViewModel 写法见[非分页网络基类](./network-base.md)。

```kotlin
/**
 * 收集请求结果并分发页面状态
 */
fun executeRequest() {
    ResultHandler.handleResultWithData(
        scope = viewModelScope,
        flow = requestApiFlow().asResult(),
        showToast = showErrorToast,
        onLoading = { onRequestStart() },
        onData = { data -> onRequestSuccess(data) },
        onError = { message, exception -> onRequestError(message, exception) },
    )
}
```

调用 `executeRequest()` 后会发生以下状态变化：

```text
executeRequest()
→ asResult() 发出 Loading
→ ResultHandler 调用 onRequestStart()
→ Repository 发出 NetworkResponse<Goods>
├── isSucceeded == true 且 data != null → Success(goods)
├── isSucceeded == false → Error(message, exception)
└── Flow 抛出异常 → Error(exception.message, exception)
```

图中的 `isSucceeded` 代表项目自己的业务成功规则；在当前示例源码中，它对应 `code == 1000`。

本页只负责解释 `Result` 与回调分发，不定义页面三态。`onRequestStart()`、`onRequestSuccess()` 和 `onRequestError()` 怎样写入 `uiState`，以及 Route、Screen 如何渲染和重试，统一在[非分页网络基类](./network-base.md)说明。

## 错误消息

`ResultHandler` 会把原始 `errorMsg` 传给 `onError`，并记录包含异常类型和堆栈的 Timber 日志。异常类型说明只追加到日志的 `additionalInfo`，不会替换 Toast 或 `onError` 的原始文案。日志中的异常类型说明包括：

| 异常 | 文案 |
| --- | --- |
| `SerializationException` | JSON 解析错误，并附带解析位置 |
| `SocketTimeoutException` | 网络连接超时 |
| `UnknownHostException` | 无法解析主机地址 |
| `IOException` | 网络 IO 异常 |
| 其他异常 | 未知异常类型 |

`showToast = true` 时还会调用 `AppSnackbar.show(errorMsg)`。`BaseNetWorkViewModel` 和分页基类默认传入 `false`，避免通用基类自动弹出全局提示。

## 何时使用哪个入口

| 需求 | 推荐入口 | 原因 |
| --- | --- | --- |
| 页面只需要非空业务数据 | `handleResultWithData` | 回调更少，业务成功判断集中处理 |
| 需要读取完整响应元信息 | `handleResult` | `onSuccess` 可以读取 `code`、`message` 和 `data` |
| 普通单对象页面 | `BaseNetWorkViewModel` | 基类已经调用 `handleResultWithData` |
| 分页页面 | `BaseNetWorkListViewModel` | 基类还会处理列表合并、页码和刷新状态 |

不要在继承网络基类后再次手动调用 `ResultHandler`，否则同一个请求可能被收集两次，页面状态也会出现两套来源。

## 如何验证结果分支

至少使用以下响应验证新接口：

| 输入 | 预期回调 |
| --- | --- |
| 满足项目业务成功规则，且页面数据非空 | `onSuccess`，随后 `onSuccessWithData` / `onData` |
| 满足项目业务成功规则，但当前模型的 `data = null` | 只执行 `onSuccess`，不执行 `onData` 和 `onError` |
| 不满足项目业务成功规则 | `onSuccess` 后执行 `onError` |
| 超时、断网或解析异常 | 执行 `onError`，异常对象非空 |
| 任意结束路径 | 执行 `onFinally` |

::: warning 成功但无数据
当前实现遇到 `isSucceeded == true` 且 `data == null` 时不会进入错误回调。必须返回内容的接口应由后端保证页面必需数据非空；允许空结果的删除、更新类接口可以使用 `handleResult` 的 `onSuccess` 处理完整响应。使用其他响应结构时，应根据真实数据字段重新定义这一分支。
:::

## 注意事项

- 不要把 `NetworkResponse.code` 的判断复制到每个 ViewModel；统一交给 `ResultHandler`，避免各页面成功条件不一致。
- `handleResult` 会在 `scope` 中启动收集协程，通常传入 `viewModelScope` 或应用级 `CoroutineScope`。
- `onFinally` 无论成功、业务错误还是异常都会执行，适合清理临时标记。
- 捕获了 HTTP 层异常并不代表业务成功；始终处理 `onError` 和 `NetworkResponse.message`。

## 下一步

继续阅读[网络请求](./network.md)，查看 Service、DataSource、Repository 如何产生本页处理的 Flow；再阅读[数据层](./data.md)，理解线程切换和依赖边界。

## 官方文档

- [Kotlin Flow 异常处理](https://kotlinlang.org/docs/flow.html#flow-exceptions)
- [Android ViewModel 生命周期](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Timber 官方仓库](https://github.com/JakeWharton/timber)
