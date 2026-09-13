# 创建页面流程

## 介绍

新增页面需要完成六个环节：定义可序列化 Route、创建 Hilt ViewModel、实现 Route → Screen → Content 页面、在功能域 Graph 注册入口、封装模块级 Navigator、从来源页面发起跳转。当前项目使用 Navigation 3 的 `NavKey` 与 `EntryProviderScope`，页面入口由 Graph 的 `entry<Route>` 映射到 `Route` Composable。

模块级 Navigator 是业务代码访问导航能力的统一入口。Route 类型、跳转方法和结果 Key 集中放在 `core/navigation/<domain>/`，ViewModel 只调用具有业务语义的方法，不直接构造 `NavKey` 或在多个页面重复导航细节。

```text
来源页面 → ViewModel → 模块 Navigator → NavigationService → AppNavigator
    → Feature Graph → Route → Screen → Content
```

下面的 `GoodsDetail` 是“新增页面练习”使用的示例名称，不是项目内的真实路由。复制流程时必须把示例模型、仓库和文案替换为真实业务内容；如果只是修改已有页面，应从 Graph 和现有 ViewModel 开始核对，不要重复创建 Route。

## 前置条件

- 确定功能域名称，例如 `demo`、`user` 或新建的 `profile`。
- 确定页面是否需要路由参数、网络请求、分页或登录拦截。
- 确认对应 Repository、DataSource 和模型已存在；Feature 不直接创建网络、数据库或本地存储客户端。

## 一、定义类型安全路由

文件位置：`core/navigation/<domain>/<Domain>Routes.kt`。路由对象必须实现 `NavKey` 并添加 `@Serializable`。无参数页面使用 `data object`，带参数页面使用 `data class`。

下面的示例在 `DemoRoutes` 中定义一个无参数页面。带参数页面使用 `data class`，并按[ViewModel 规范](./viewmodel.md#路由参数与-assisted-inject)接入 Assisted Factory。

```kotlin
package com.joker.kit.core.navigation.demo

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Demo 模块路由
 */
object DemoRoutes {
    /**
     * 商品详情路由
     */
    @Serializable
    data object GoodsDetail : NavKey
}
```

验证方式：编译时确认 Route 可作为 `entry<DemoRoutes.GoodsDetail>` 的类型参数；如果参数类型无法序列化，优先检查 Kotlin Serialization 配置和字段类型。

## 二、实现页面 ViewModel

文件位置：`feature/<domain>/viewmodel/<PageName>ViewModel.kt`。根据页面类型选择 `BaseViewModel`、`BaseNetWorkViewModel<T>` 或 `BaseNetWorkListViewModel<T>`。网络请求必须通过 Repository 返回的 `Flow`。

以下示例展示无网络页面的最小 ViewModel；网络页面应参考[ViewModel 规范](./viewmodel.md)补充基类和 Repository：

```kotlin
package com.joker.kit.feature.demo.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 商品详情页面 ViewModel
 */
@HiltViewModel
class GoodsDetailViewModel @Inject constructor() : BaseViewModel()
```

如果页面需要把参数注入 ViewModel，当前项目采用 Navigation 3 entry 提供的 `navKey` 配合 Hilt Assisted Factory；具体写法见[ViewModel 规范](./viewmodel.md#路由参数与-assisted-inject)。

## 三、实现 Route、Screen 和内容层

文件位置：`feature/<domain>/view/<PageName>Screen.kt`。Route 使用 `hiltViewModel()` 获取页面 ViewModel；Screen 接收状态和回调，负责外层框架与缺省状态；Content 绘制最终业务内容。下面是可复制的无网络页面示例：

```kotlin
package com.joker.kit.feature.demo.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.annotation.ScreenPreview
import com.joker.kit.core.annotation.ScreenPreviewDark
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import top.yukonga.miuix.kmp.basic.Text
import com.joker.kit.feature.demo.viewmodel.GoodsDetailViewModel

/**
 * 商品详情页面路由入口
 *
 * @param viewModel 页面 ViewModel
 */
@Composable
internal fun GoodsDetailRoute(
    viewModel: GoodsDetailViewModel = hiltViewModel(),
) {
    // 页面暂无业务状态，Route 只负责创建 ViewModel 并进入 Screen。
    GoodsDetailScreen()
}

/**
 * 商品详情页面骨架
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GoodsDetailScreen() {
    AppScaffold(
        titleText = "商品详情",
        onBackClick = { navigateBack() },
    ) {
        GoodsDetailContent()
    }
}

/**
 * 商品详情页面内容
 */
@Composable
private fun GoodsDetailContent() {
    Text(text = "商品详情")
}

/** 商品详情页面浅色预览。 */
@ScreenPreview
@Composable
private fun GoodsDetailPreview() {
    AppTheme {
        GoodsDetailScreen()
    }
}

/** 商品详情页面深色预览。 */
@ScreenPreviewDark
@Composable
private fun GoodsDetailPreviewDark() {
    AppTheme(darkTheme = true) {
        GoodsDetailScreen()
    }
}
```

示例省略了具体业务数据与资源；正式页面应使用真实模型、`AppScaffold` 和资源字符串。顶部栏直接调用 `navigateBack()` 完成普通返回；业务操作仍由 Route 传入 `viewModel::method`，不要在 Content 内实例化 ViewModel 或发起业务导航。

当页面增加 `StateFlow` 后，Route 中的每个 `collectAsState()` 上方都应注明具体状态，例如“收集页面状态”或“收集下拉刷新状态”。ViewModel 的可变状态源、公开只读状态和重写属性分别添加 KDoc；View 与 ViewModel 的局部变量在声明上方说明用途。无状态页面不需要为了凑结构创建空 `StateFlow`。

## 四、注册功能域 Graph

文件位置：`feature/<domain>/navigation/<Domain>Graph.kt`。使用 `EntryProviderScope<NavKey>`，并让每个 Route 对应一个 `entry`：

```kotlin
package com.joker.kit.feature.demo.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.joker.kit.core.navigation.demo.DemoRoutes
import com.joker.kit.feature.demo.view.GoodsDetailRoute

/**
 * Demo 模块导航图
 */
fun EntryProviderScope<NavKey>.demoGraph() {
    entry<DemoRoutes.GoodsDetail> {
        GoodsDetailRoute()
    }
}
```

然后在 `AppNavHost.appEntryProvider` 中调用 `demoGraph()`；已有功能域只需在对应 Graph 增加 entry，不要重复注册。

## 五、使用模块级 Navigator（推荐）

文件位置：`core/navigation/<domain>/<Domain>Navigator.kt`。虽然业务代码可以直接调用顶层 `navigate(route)`，但页面跳转应优先由模块级 Navigator 统一封装。Navigator 负责把具有业务语义的方法转换为具体 Route，使调用方不需要了解路由类型、参数构造和回退栈操作。

同一业务域的导航契约集中在一个目录下：

- `<Domain>Routes.kt`：定义目标 Route 和页面参数；
- `<Domain>Navigator.kt`：提供 `to<PageName>()` 等语义化跳转方法；
- `<Domain>ResultKey.kt`：按需定义返回结果模型与 `NavigationResultKey<T>`。

`GoodsDetail` 属于 Demo 功能域，因此在 `DemoNavigator` 中增加对应方法：

```kotlin
package com.joker.kit.core.navigation.demo

import com.joker.kit.core.navigation.navigate

/**
 * Demo 模块导航封装
 */
object DemoNavigator {
    /**
     * 打开商品详情页
     */
    fun toGoodsDetail() {
        // 将业务动作转换为类型安全路由。
        navigate(DemoRoutes.GoodsDetail)
    }
}
```

带参页面由 Navigator 方法接收明确类型的参数，再构造对应的 `NavKey`；需要结果回传时，把 `NavigationResultKey<T>` 和结果模型放在同一业务域目录。完整写法见[参数传递与结果回传](../导航/result.md)。

## 六、从来源页面发起跳转

来源页面通过事件回调把用户操作交给 ViewModel，ViewModel 再调用模块 Navigator。文件位置示例：`feature/demo/viewmodel/GoodsListViewModel.kt`。

```kotlin
package com.joker.kit.feature.demo.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.navigation.demo.DemoNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 商品列表页面 ViewModel
 */
@HiltViewModel
class GoodsListViewModel @Inject constructor() : BaseViewModel() {
    /**
     * 处理商品点击事件
     */
    fun onGoodsClick() {
        // 通过模块导航入口打开商品详情页。
        DemoNavigator.toGoodsDetail()
    }
}
```

Route 将 `viewModel::onGoodsClick` 传给 Screen，Screen 再把回调交给 Content。Content 只上报点击事件，不直接调用 `DemoNavigator`、`NavigationService` 或修改 `backStack`。这样可以让页面布局保持无导航依赖，并在 ViewModel 中统一处理业务动作。

需要登录的路由在 `core/navigation/RouteInterceptor.kt` 的 `loginRequiredRouteTypes` 中登记。未登录时 `AppNavigator` 会把目标路由解析为 `AuthRoutes.Login`；不要在每个页面重复实现登录判断。

如果页面需要带参数，先完成[路由配置](../导航/router.md)中的 `NavKey + AssistedInject`，再把 `navKey` 从 Graph 传入 Route。本项目的 Navigation 3 页面不使用 `SavedStateHandle.toRoute()` 读取路由参数。

## 验证结果

1. 运行 `./gradlew :app:assembleDebug`，确认 Route、Navigator、Graph、ViewModel 和 Compose 示例均可编译。
2. 从来源页面触发 ViewModel 事件，确认模块 Navigator 被调用、Graph 已聚合且目标页面可显示。
3. 对单对象网络页面验证 Loading、Success、Error 和重试；对分页列表额外验证 Empty、刷新与加载更多。
4. 对新增页面运行 `@ScreenPreview` 与 `@ScreenPreviewDark`，确认 Content 不依赖 ViewModel；多组业务数据通过 `@PreviewParameter` 注入。

## 常见问题

### 页面找不到或跳转无效

检查 ViewModel 是否调用正确的模块 Navigator、Navigator 是否构造目标 Route、Route 是否在对应 Graph 使用 `entry<...>` 注册，以及该 Graph 是否在 `AppNavHost` 聚合。

### Hilt 无法创建 ViewModel

检查类是否标记 `@HiltViewModel`、构造函数是否使用 `@Inject` 或 Assisted Factory，并确认页面由 `hiltViewModel()` 获取，而不是手动创建。

### 页面缺少 Content 层

将 Screen 中的最终业务布局拆到 `<PageName>Content`。Screen 只保留按页面需要使用的 `Scaffold`、页面骨架、Loading、Empty、Error 和 Success 分支，并在成功分支调用 Content。

## 相关链接

- [注解](../框架核心/annotation.md)：选择当前已有的页面、组件和多设备预览注解。
- [数据层：预览数据](../框架核心/data.md#预览数据)：为复杂页面提供多组 Preview 参数。
- [路由配置](../导航/router.md)：了解 Route、模块 Navigator、Feature Graph 和应用级 Graph 的职责。
- [参数传递与结果回传](../导航/result.md)：集中管理 Navigator 参数、结果模型和结果 Key。
- [Navigation 3：注册 Entry](https://developer.android.com/guide/navigation/navigation-3)
- [Hilt ViewModel 注入](https://developer.android.com/training/dependency-injection/hilt-jetpack)
- [Compose 状态与事件](https://developer.android.com/develop/ui/compose/state)
