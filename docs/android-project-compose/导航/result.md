# 参数传递与结果回传

## 介绍

页面之间的数据通信有两个方向：进入目标页面时向前传递参数，完成操作后向上一页回传结果。这两个方向使用不同机制，不能混为同一种“传参”。

| 方向 | 适用场景 | 当前机制 |
| --- | --- | --- |
| 页面 A → 页面 B | 打开详情页时传入商品 ID、筛选条件等初始参数 | 参数声明在 `NavKey`，由 Graph 传给 Route 和 ViewModel |
| 页面 B → 页面 A | 选择完成、编辑成功或通知上一页刷新 | `NavigationResultKey<T>`、`popBackStackWithResult` 与 `resultEvents` |

普通参数属于目标路由本身，会跟随 `NavKey` 进入回退栈；返回结果是一次性事件。需要跨页面长期共享、跨进程恢复或离线保存的数据，应放入 Repository、Room 或本地存储，不应依赖导航通信。

## 普通参数传递

当前项目的真实示例是 `DemoRoutes.NavigationWithArgs(goodsId)`。参数从调用方到页面的完整链路如下：

```text
DemoNavigator.toNavigationWithArgs(goodsId)
  → navigate(DemoRoutes.NavigationWithArgs(goodsId))
  → DemoGraph 的 entry<NavigationWithArgs> 收到 key
  → NavigationWithArgsRoute(navKey = key)
  → Assisted Factory 创建 NavigationWithArgsViewModel
  → ViewModel 读取 navKey.goodsId
  → Screen 渲染 goodsId
```

### 一、在 NavKey 中声明参数

文件位置：`core/navigation/demo/DemoRoutes.kt`。

带参数路由使用 `@Serializable data class`，每个字段都是该页面的输入契约：

```kotlin
package com.joker.kit.core.navigation.demo

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Demo 模块路由
 */
object DemoRoutes {
    /**
     * 带参跳转页面
     *
     * @property goodsId 商品 ID
     */
    @Serializable
    data class NavigationWithArgs(
        val goodsId: Long,
    ) : NavKey
}
```

参数类型必须支持 Kotlin Serialization。页面只需要商品详情时优先传递 `goodsId`，再由目标页面通过 Repository 读取最新数据；不要把图片列表、完整详情对象等大数据直接塞进路由。

### 二、跳转时构造带参路由

文件位置：`core/navigation/demo/DemoNavigator.kt`。

调用方不拼接字符串，而是创建路由对象：

```kotlin
package com.joker.kit.core.navigation.demo

import com.joker.kit.core.navigation.navigate

/**
 * Demo 模块导航入口
 */
object DemoNavigator {
    /**
     * 打开带参页面
     *
     * @param goodsId 商品 ID
     */
    fun toNavigationWithArgs(goodsId: Long = 0) {
        // goodsId 成为目标 NavKey 的一部分。
        navigate(DemoRoutes.NavigationWithArgs(goodsId = goodsId))
    }
}
```

真实业务不应随意保留 `0` 作为默认 ID。参数缺失属于错误时，移除默认值，让调用方在编译期必须传入；只有业务明确允许默认项时才提供默认值。

### 三、Graph 把 NavKey 交给 Route

文件位置：`feature/demo/navigation/DemoGraph.kt`。

`entry` lambda 中的 `key` 已经是反序列化后的 `DemoRoutes.NavigationWithArgs`：

```kotlin
package com.joker.kit.feature.demo.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.joker.kit.core.navigation.demo.DemoRoutes
import com.joker.kit.feature.demo.view.NavigationWithArgsRoute

/**
 * 注册 Demo 模块页面
 */
fun EntryProviderScope<NavKey>.demoGraph() {
    entry<DemoRoutes.NavigationWithArgs> { key ->
        // Graph 只完成路由与页面入口的交接。
        NavigationWithArgsRoute(navKey = key)
    }
}
```

不要在 Graph 中发起请求或修改业务状态。Graph 只负责把类型安全参数交给 Route。

### 四、通过 Assisted Factory 创建 ViewModel

当前项目使用 Hilt Assisted Injection，把 Graph 传来的 `navKey` 注入 ViewModel。文件位置：`feature/demo/viewmodel/NavigationWithArgsViewModel.kt`。

```kotlin
package com.joker.kit.feature.demo.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.navigation.demo.DemoRoutes
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * 带参跳转页面 ViewModel
 *
 * @param navKey 页面路由参数
 */
@HiltViewModel(assistedFactory = NavigationWithArgsViewModel.Factory::class)
class NavigationWithArgsViewModel @AssistedInject constructor(
    @Assisted private val navKey: DemoRoutes.NavigationWithArgs,
) : BaseViewModel() {

    /** 商品 ID。 */
    val goodsId: Long = navKey.goodsId

    /**
     * 带参 ViewModel 工厂
     */
    @AssistedFactory
    interface Factory {
        /**
         * 创建 ViewModel
         *
         * @param navKey 页面路由参数
         * @return ViewModel 实例
         */
        fun create(navKey: DemoRoutes.NavigationWithArgs): NavigationWithArgsViewModel
    }
}
```

Route 使用对应 Factory 创建 ViewModel，再把 ViewModel 暴露的数据交给 Screen。文件位置：`feature/demo/view/NavigationWithArgsScreen.kt`。

```kotlin
package com.joker.kit.feature.demo.view

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.navigation.demo.DemoRoutes
import com.joker.kit.feature.demo.viewmodel.NavigationWithArgsViewModel

/**
 * 带参跳转页面 Route
 *
 * @param navKey 页面路由参数
 * @param viewModel 页面 ViewModel
 */
@Composable
internal fun NavigationWithArgsRoute(
    navKey: DemoRoutes.NavigationWithArgs,
    viewModel: NavigationWithArgsViewModel = hiltViewModel<
        NavigationWithArgsViewModel,
        NavigationWithArgsViewModel.Factory
    >(creationCallback = { factory -> factory.create(navKey) }),
) {
    // 将路由参数交给 Screen 渲染。
    NavigationWithArgsScreen(goodsId = viewModel.goodsId)
}
```

这组参数由 Graph 提供的 `key` 传入 Assisted Factory，不通过 `SavedStateHandle.toRoute()` 读取。

### 五、Screen 只接收可渲染数据

Screen 不读取回退栈，也不持有 `NavKey`。它只接收 Route 已经整理好的页面参数：

```kotlin
package com.joker.kit.feature.demo.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import top.yukonga.miuix.kmp.basic.Text

/**
 * 带参跳转页面
 *
 * @param goodsId 商品 ID
 */
@Composable
internal fun NavigationWithArgsScreen(
    goodsId: Long,
) {
    AppScaffold(
        titleText = "带参跳转",
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        // Content 消费 Scaffold 提供的安全区内边距。
        NavigationWithArgsContent(
            contentPadding = paddingValues,
            goodsId = goodsId,
        )
    }
}

/**
 * 带参跳转页面内容
 *
 * @param contentPadding Scaffold 提供的安全区内边距
 * @param goodsId 商品 ID
 */
@Composable
private fun NavigationWithArgsContent(
    contentPadding: PaddingValues,
    goodsId: Long,
) {
    MiuixPageLazyColumn(contentPadding = contentPadding) {
        item {
            Text(text = "传递的商品 ID：$goodsId")
        }
    }
}
```

普通参数按照上述链路完成 A → B 的单向传递：Route 从 ViewModel 读取参数，Screen 组织页面外层，Content 显示最终业务内容。路由如何注册和聚合由[路由配置](./router.md)说明；本页只说明参数值如何到达页面。

## 结果回传核心 API

返回结果用于子页面完成操作后向上一级携带类型安全数据。发送方调用 `popBackStackWithResult(key, result)`，`AppNavigator` 通过 `resultEvents(key)` 将结果分发给父页面 ViewModel。当前实现使用不带 replay 的 `MutableSharedFlow`，因此接收方必须在发送结果前建立收集。

| API | 类型或默认值 | 说明 |
| --- | --- | --- |
| `NavigationResultKey<T>.key` | Key 对象的完全限定类名 | 过滤同一业务结果的默认字符串标识。 |
| `serialize(value)` | 默认直接返回对象 | 将 `T` 转为结果流的底层值。 |
| `deserialize(raw)` | 默认强制转换为 `T` | 将结果流底层值还原为业务类型。 |
| `popBackStackWithResult(key, result)` | 泛型 `T` | 先分发结果，再回退一层。 |
| `resultEvents(key)` | `Flow<T>` | 按 Key 过滤并反序列化结果。 |
| `MutableSharedFlow` 缓冲 | `extraBufferCapacity = 32`、`replay = 0` | 缓冲慢消费者，但不向后加入的订阅者重放旧结果。 |

## 结果模型与 Key

`NavigationResultKey<T>` 用泛型 `T` 绑定结果类型，并以 Key 对象的完全限定类名作为默认字符串标识。简单类型可以直接使用默认的透传序列化；复杂对象应在专用 Key 中覆盖 `serialize` 和 `deserialize`。

文件位置：`core/navigation/demo/DemoResultKey.kt`。

```kotlin
package com.joker.kit.core.navigation.demo

import com.joker.kit.core.navigation.NavigationResultKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Demo 结果 Key。
 */
object DemoResultKey : NavigationResultKey<DemoResult> {
    /**
     * 将结果编码为 JSON 字符串。
     *
     * @param value 待编码结果
     * @return JSON 字符串
     */
    override fun serialize(value: DemoResult): Any {
        // 复杂结果通过稳定格式跨越导航事件边界。
        return Json.encodeToString(value)
    }

    /**
     * 将导航事件值还原为结果对象。
     *
     * @param raw 导航事件底层值
     * @return 还原后的 Demo 结果
     */
    override fun deserialize(raw: Any): DemoResult {
        // 编码与解码类型必须保持一致，错误类型会在此处抛出异常。
        return Json.decodeFromString(raw as String)
    }
}

/**
 * Demo 结果数据。
 *
 * @param id 结果 ID
 * @param message 结果信息
 */
@Serializable
data class DemoResult(
    val id: Long,
    val message: String,
)
```

基础类型可以使用 `NavigationResultKey` 默认的对象透传；复杂对象使用项目已有的 `kotlinx.serialization`，不要自行拼接不稳定字符串。

## 公共刷新结果

项目已提供通用 `RefreshResult` 和 `RefreshResultKey`，适用于“子页面操作成功，父页面重新请求”的场景。

文件位置：`core/navigation/NavigationResult.kt` 与 `core/navigation/RefreshResultKey.kt`。

```kotlin
/**
 * 页面刷新结果。
 *
 * @param refresh 是否需要刷新
 */
data class RefreshResult(
    val refresh: Boolean? = null,
)

/**
 * 页面刷新结果 Key。
 */
object RefreshResultKey : NavigationResultKey<RefreshResult>
```

当 `refresh = true` 时，`BaseNetWorkViewModel.observeRefreshState(...)` 和 `BaseNetWorkListViewModel.observeRefreshState(...)` 会触发 `executeRequest()`；`false` 或 `null` 不会触发刷新。

## 发送结果

### ViewModel

子页面 ViewModel 负责构造结果并发送。真实 Demo 使用 `DemoResultKey` 将 `DemoResult` 编码为 JSON 字符串，再返回上一页。

文件位置：`feature/demo/viewmodel/NavigationResultViewModel.kt`。

以下代码是当前发送方 ViewModel 的完整实现。

```kotlin
package com.joker.kit.feature.demo.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.navigation.demo.DemoResult
import com.joker.kit.core.navigation.demo.DemoResultKey
import com.joker.kit.core.navigation.popBackStackWithResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 结果回传示例 ViewModel。
 */
@HiltViewModel
class NavigationResultViewModel @Inject constructor() : BaseViewModel() {
    /**
     * 发送 Demo 结果并返回上一页。
     */
    fun sendResultAndBack() {
        // 控制器先分发结果，再从当前回退栈移除页面。
        popBackStackWithResult(
            DemoResultKey,
            DemoResult(id = 9527, message = "这是回传的结果"),
        )
    }
}
```

### View

View 只把按钮事件交给 ViewModel，不直接操作结果流。

文件位置：`feature/demo/view/NavigationResultScreen.kt`。

```kotlin
package com.joker.kit.feature.demo.view

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.feature.demo.viewmodel.NavigationResultViewModel

/**
 * 结果回传页面 Route。
 *
 * @param viewModel 当前页面 ViewModel
 */
@Composable
internal fun NavigationResultRoute(
    viewModel: NavigationResultViewModel = hiltViewModel(),
) {
    // View 只绑定事件，结果构造和导航由 ViewModel 负责。
    NavigationResultScreen(onSendResult = viewModel::sendResultAndBack)
}
```

验证方式：在结果示例页点击“回传结果并返回”，父页面的 `demoResult` 状态应收到 `DemoResult(id = 9527, ...)`。

## 接收结果

### 父页面 ViewModel

父页面在初始化时只注册一次 `resultEvents(DemoResultKey)`。当前 `NavigationViewModel` 将接收到的结果写入 `StateFlow`，由页面订阅并展示。

文件位置：示例可放在 `feature/main/viewmodel/ResultReceiverViewModel.kt`；现有 `NavigationViewModel.kt` 在同一个 `init` 中采用相同收集方式。

以下代码展示一个可独立使用的接收方 ViewModel。当前工程的 `NavigationViewModel` 采用同样写法，并另外维护页面卡片与登录状态。

```kotlin
package com.joker.kit.feature.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.navigation.demo.DemoResult
import com.joker.kit.core.navigation.demo.DemoResultKey
import com.joker.kit.core.navigation.resultEvents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 接收导航结果的父页面 ViewModel。
 */
@HiltViewModel
class ResultReceiverViewModel @Inject constructor() : BaseViewModel() {
    /** 最近一次导航结果的可变状态源。 */
    private val _demoResult = MutableStateFlow<DemoResult?>(null)

    /** 最近一次收到的 Demo 结果。 */
    val demoResult = _demoResult.asStateFlow()

    init {
        observeDemoResult()
    }

    /**
     * 收集 Demo 结果事件。
     */
    private fun observeDemoResult() {
        viewModelScope.launch {
            // 在子页面发送结果前建立收集，避免 replay = 0 时错过事件。
            resultEvents(DemoResultKey).collect { result ->
                _demoResult.value = result
            }
        }
    }
}
```

结果接收的实际实现链路为：`resultEvents(key)` → `AppNavigator.resultEvents(key)` → 按 `key.key` 过滤 → 调用 `key.deserialize(rawValue)` 还原 `T`。

## 参数传递与结果回传组合

同一组页面可以同时使用两个方向。例如，列表页打开编辑页时传入 `goodsId`，编辑页保存成功后再通知列表页刷新：

```text
列表页
  → DemoRoutes.NavigationWithArgs(goodsId = 123)  正向传入目标 ID
编辑页
  → 根据 goodsId 读取并保存数据
  → popBackStackWithResult(RefreshResultKey, RefreshResult(true))
列表页
  → 收到一次刷新结果并重新请求
```

正向参数回答“目标页面处理哪条数据”，返回结果回答“目标页面完成了什么”。不要把完整编辑后对象来回传递；Repository 才是业务数据的权威来源，返回结果通常只需携带刷新标记、选中 ID 或轻量结果模型。

## 刷新结果示例

使用网络基类时，父页面 ViewModel 可以调用一次 `observeRefreshState()`，无需手动收集刷新结果。

父页面 ViewModel：

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
 * 使用公共刷新结果的 ViewModel 示例。
 *
 * @param goodsRepository 商品仓库
 */
@HiltViewModel
class RefreshableGoodsViewModel @Inject constructor(
    private val goodsRepository: GoodsRepository
) : BaseNetWorkViewModel<Goods>() {
    init {
        // 默认监听 RefreshResultKey；refresh = true 时自动执行请求。
        observeRefreshState()
        executeRequest()
    }

    /**
     * 提供页面真实的 Repository 请求流。
     *
     * @return 网络响应数据流
     */
    override fun requestApiFlow(): Flow<NetworkResponse<Goods>> =
        goodsRepository.getGoodsInfo("1")
}
```

子页面 ViewModel：

```kotlin
package com.joker.kit.feature.demo.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.navigation.RefreshResult
import com.joker.kit.core.navigation.RefreshResultKey
import com.joker.kit.core.navigation.popBackStackWithResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 发送刷新信号的 ViewModel 示例。
 */
@HiltViewModel
class EditViewModel @Inject constructor() : BaseViewModel() {
    /**
     * 保存成功后通知父页面刷新。
     */
    fun saveSucceeded() {
        popBackStackWithResult(
            RefreshResultKey,
            RefreshResult(refresh = true),
        )
    }
}
```

## 注意事项

- 正向参数属于目标 `NavKey`，返回结果属于一次性事件；不要用 ResultKey 模拟页面初始参数。
- 路由参数优先传递 ID、枚举和轻量筛选条件；目标页面需要完整业务对象时，通过 Repository 按 ID 获取最新数据。
- `NavigationResultKey` 的 Key 字符串必须全局唯一；默认完全限定类名通常足够，仍应避免为不同语义复用同一个 Key。
- 自定义 `serialize` 与 `deserialize` 必须成对实现，并保证底层类型一致；默认 `deserialize` 是类型强转，错误类型会抛出异常。
- `dispatchResult` 使用 `tryEmit` 且没有检查返回值；缓冲区被慢消费者占满时，新事件可能发送失败。结果事件也不是持久化状态，不要用它传递必须跨进程或跨重启保存的数据。
- 结果发送顺序是“发事件，再回退”；父页面应在子页面发送结果前建立订阅。
- 一个业务语义使用一个独立 Key，避免多个页面共享无法区分的结果模型。

## 相关链接

- [导航概览](./index.md)：查看导航服务和模块边界。
- [Android Navigation 3 官方指南](https://developer.android.com/guide/navigation/navigation-3)：了解状态驱动导航和返回栈。
- [Kotlin Serialization 官方指南](https://kotlinlang.org/docs/serialization.html)：查看复杂结果的 JSON 序列化方式。
