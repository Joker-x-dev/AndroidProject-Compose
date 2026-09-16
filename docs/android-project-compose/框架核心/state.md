# 全局状态

## 介绍

`core/state` 存放需要跨页面共享的应用级状态。当前包含用户状态持有者 `UserState`、计数示例 `DemoCounterState`、全局提示入口 `AppSnackbar` 和全局对话框状态 `AppDialogState`。页面局部状态仍应保留在 Feature ViewModel 中。

全局状态解决的是“多个页面需要观察同一份数据”，不是“任何状态都放到单例”。搜索文本、弹窗开关和一次性加载状态仍应放在对应 ViewModel；只有跨页面且需要应用进程内共享的数据才进入 `core/state`。

## UserState 的依赖与生命周期

以下链路展示 `UserState` 的三个数据来源和协程作用域。

```text
UserState
├── AuthStoreRepository       → MMKV auth_info
├── UserInfoStoreRepository   → MMKV user_info
├── UserInfoRepository        → 用户信息网络接口
└── @ApplicationScope CoroutineScope
```

`AppStateModule` 提供 `SupervisorJob() + Dispatchers.Default` 的应用级作用域。`Application.onCreate()` 在 `MMKVUtils.init(this)` 后显式调用 `userState.initialize()`，因此恢复本地登录状态不会早于 MMKV 初始化。

`SupervisorJob` 保证某个刷新任务失败时不会取消其他应用级任务；它不会替代 Repository 的 IO 线程切换，也不会让网络请求自动重试。

## UserState 状态流

| 名称 | 类型 | 初始值 | 含义 |
| --- | --- | --- | --- |
| `isLoggedIn` | `StateFlow<Boolean>` | `false` | 本地 Token 存在且未过期 |
| `userId` | `StateFlow<Long>` | `0L` | 当前用户 ID |
| `auth` | `StateFlow<Auth?>` | `null` | 当前认证令牌信息 |
| `userInfo` | `StateFlow<User?>` | `null` | 当前用户资料 |

这些属性由 `asStateFlow()` 暴露，页面只能收集，不能直接修改。

## UserState 初始化与写入

### 初始化

`initialize()` 在应用作用域启动 `initializeState()`：读取 `AuthStoreRepository`，判断登录状态；仅当已登录时读取用户资料并填充 `userId`。

### 登录成功

`updateUserState(auth, user)` 先分别写入认证和用户资料仓库，再更新 `_auth`、`_userInfo`、`_userId` 和 `_isLoggedIn`。

### 更新 Token 或资料

- `updateAuth(auth)` 保存新认证信息并保持登录态。
- `updateUserInfo(user)` 保存完整用户资料，并同步资料与用户 ID 状态流。
- `refreshUserInfo()` 在已登录时调用网络仓库，将成功数据交给 `updateUserInfo`。

### 退出登录

`logout()` 清除认证和用户资料本地缓存，再把四个 StateFlow 重置为未登录初始值。

## UserState 的 ViewModel 用法

文件位置：`feature/main/viewmodel/NavigationViewModel.kt`。

当前 Navigation 页面直接注入 `UserState`，把全局登录状态作为只读 StateFlow 暴露给 Route。下面省略了导航结果相关状态。

```kotlin
package com.joker.kit.feature.main.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Navigation 页面 ViewModel
 *
 * @param userState 用户状态
 */
@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val userState: UserState
) : BaseViewModel() {

    /** 全局登录状态 */
    val isLoggedIn: StateFlow<Boolean> = userState.isLoggedIn
}
```

## UserState 的 View 用法

文件位置：`feature/main/view/NavigationScreen.kt`。

```kotlin
package com.joker.kit.feature.main.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.feature.main.viewmodel.NavigationViewModel

/**
 * Navigation 页面路由
 *
 * @param viewModel Navigation 页面 ViewModel
 */
@Composable
internal fun NavigationRoute(
    viewModel: NavigationViewModel = hiltViewModel()
) {
    // Route 收集应用级登录状态并转换为 Screen 参数
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    NavigationScreen(
        cards = emptyList(),
        isLoggedIn = isLoggedIn,
        onCardClick = {}
    )
}
```

当前完整 Route 还会收集卡片和导航回传结果。Screen 只接收值和回调；登录状态为 `true` 时展示登录提示卡片。

调用 `userState.updateUserInfo(user)` 后，所有已订阅 `userInfo` 的 Route 都会收到新对象；调用 `logout()` 后则依次清理本地仓库并把 `isLoggedIn`、`auth`、`userInfo` 和 `userId` 恢复到初始值。UI 不需要手动刷新页面。

## UserState API

| 方法 | 参数 | 作用 |
| --- | --- | --- |
| `initialize()` | 无 | 应用启动时异步恢复本地状态 |
| `updateUserState(auth, user)` | `Auth`, `User` | 登录成功后同时写入认证和用户资料 |
| `updateAuth(auth)` | `Auth` | 更新 Token 与登录标记 |
| `updateUserInfo(user)` | `User` | 更新本地资料和内存状态 |
| `refreshUserInfo()` | 无 | 已登录时请求网络资料并更新状态 |
| `shouldRefreshToken()` | 无，挂起 | 委托认证仓库判断刷新窗口 |
| `logout()` | 无，挂起 | 清除本地缓存并重置状态 |

## UserState 注意事项

- `UserState` 是 `@Singleton`，但它的 `initialize()` 仍需由 `Application` 手动调用一次；当前实现不会在构造函数中自动初始化。
- `refreshUserInfo()` 在未登录时直接返回，不会发起网络请求。
- `updateAuth()` 将 `_isLoggedIn` 设为 `true`，调用方应确保传入的 `Auth` 有效。
- 应用级状态使用 `Dispatchers.Default`；具体网络与数据库线程切换仍由 Repository 或 DataSource 负责。
- `refreshUserInfo()` 只在当前 `isLoggedIn` 为 `true` 时发起请求；网络失败由 `ResultHandler` 记录和提示，旧的 `userInfo` 不会因为失败自动清空。
- `initialize()` 是异步且无返回值的启动操作；需要等待初始化完成的页面应观察 `isLoggedIn`、`auth` 和 `userInfo` 的变化，不要假设调用后立即完成。

## AppSnackbar 与 AppDialogState

`AppSnackbar` 提供非挂起、非阻塞的全局提示入口，任意线程与任意业务层均可调用：

```kotlin
AppSnackbar.show("操作成功")
AppSnackbar.show(
    message = "删除完成",
    actionLabel = "撤销",
    onAction = { restore() },
)
```

`Application.onCreate()` 通过 `AppSnackbar.initialize(this)` 绑定应用上下文，用于解析字符串资源；消息由 `MainActivity` 中的 `AppSnackbarHost` 统一展示。宿主不存在时消息丢弃，不会阻塞调用线程。

`AppDialogState` 基于 Miuix Dialog 封装，参数命名与 Miuix 保持一致，以 `StateFlow` 驱动全局对话框，任意业务层一行代码即可触发，页面无需声明任何弹窗状态：

```kotlin
// 确认框
AppDialogState.confirm(
    title = "确认退出",
    summary = "确定要退出登录吗？",
    onConfirm = { logout() },
)

// 自定义内容与按钮的对话框
AppDialogState.show(
    title = "Dialog",
    summary = "点击按钮关闭对话框。",
    actions = listOf(
        AppDialogAction(text = "取消", onClick = { AppDialogState.hide() }),
        AppDialogAction(text = "确定", onClick = { AppDialogState.hide() }, isPrimary = true),
    ),
)
```

`confirm` 的 `onCancel` 为 `null` 时只展示确定按钮；`confirmText`、`cancelText` 与 `confirmColor` 可覆盖按钮文案和颜色。`show` 支持 `actions` 自定义按钮、`maxWidth` 控制宽度、`wide` 在横屏时切换为标题摘要与按钮的双列布局、`largeScreen` 强制居中呈现。`dismissOnClickOutside` 控制点击遮罩或返回键是否关闭。Dialog 组件位于 `core/ui/component/dialog`，本状态对象只维护配置，不直接渲染界面。

## DemoCounterState

`DemoCounterState` 用于展示最小全局状态，不依赖 Repository：

| 方法 | 行为 |
| --- | --- |
| `increase()` | 计数加 1 |
| `decrease()` | 计数减 1，最低为 0 |
| `reset()` | 重置为 0 |

它的 `count` 也是只读 `StateFlow`。新增业务全局状态时，先确认它确实需要跨页面共享，再仿照 `UserState` 使用 `@Singleton` 和 `@ApplicationScope`。

## 官方文档

- [在 Compose 中收集 StateFlow](https://developer.android.com/develop/ui/compose/state#collect)
- [StateFlow 与 SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- [应用架构中的数据层](https://developer.android.com/topic/architecture/data-layer)
