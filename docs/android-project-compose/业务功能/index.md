# Feature 模块概览

## 介绍

Feature 层承载应用业务页面，并把页面注册、Compose UI、ViewModel 与业务展示数据组织在同一个功能域内。路由类型本身位于 `core/navigation`，Feature 只在自己的 Graph 中把路由映射到页面。当前项目只有一个 Gradle 模块 `:app`；`feature/` 是包级分层，不是可单独编译的 Gradle 子模块。Feature 可以调用 `core/` 的通用能力，但不应绕过 Repository 直接访问网络、数据库或本地存储实现。

本组文档假定读者已经完成 [Core](../框架核心/index.md) 和 [Navigation](../导航/index.md) 的阅读。Feature 不重新定义三态、分页或回退栈，而是把这些能力组合成一个可进入、可验证的业务页面。

## 模块组成

当前源码包含四个功能域：

| 功能域 | 目录 | 已注册页面 | 主要职责 |
| --- | --- | --- | --- |
| `auth` | `feature/auth/` | `AuthRoutes.Login` | 登录与退出登录示例 |
| `demo` | `feature/demo/` | `NetworkDemo`、`NetworkListDemo`、`Database`、`LocalStorage`、`StateManagement`、`NetworkRequest`、`NavigationWithArgs`、`NavigationResult`、`ScreenAdaptDemo` | 网络、分页、数据库、本地存储、状态和导航示例 |
| `main` | `feature/main/` | `MainRoutes.Main` 与其内部页面入口 | 应用首页与底部导航，聚合 Core、Navigation、Expand 页面 |
| `common` | `feature/common/` | About 页面 | 独立的应用信息、开发者、项目地址和相关资源页面 |
| `user` | `feature/user/` | `UserRoutes.Info` | 用户信息与登录态操作 |

每个功能域按需创建 `navigation/`、`view/`、`viewmodel/` 和 `component/`。`component/`、`data/`、`model/`、`skeleton/` 不是所有功能域都必须具备；当前 `demo` 使用了 `component/`、`skeleton/`，`main` 还包含 `data/` 与 `model/`。

## 页面运行链路

页面的主链路是：

```text
Route → Screen → Content
  │       │              │
  │       │              └─ 只接收参数并绘制 UI
  │       └─ 组合页面骨架、状态分支与回调
  └─ 注入 ViewModel，收集 StateFlow，转发事件
```

每个页面都保留三层。Route 只收集状态并绑定 ViewModel 事件；Screen 按页面需要组织 `Scaffold`、页面骨架和 Loading、Empty、Error 等状态容器；Content 只绘制普通页面或成功状态下的最终业务内容。`MainScreen` 是无 `Scaffold` 的顶级页面容器，但仍保留 `MainContent`。

页面完成的最小闭环是：

```text
Route 已注册 → ViewModel 能被 Hilt 创建 → Screen 能渲染预览
→ 用户事件能到达 ViewModel → Repository / Navigation 调用可验证
```

## 与 Core 的关系

- 路由类型放在 `core/navigation/<domain>/*Routes.kt`，Feature 的 `navigation/*Graph.kt` 只负责把路由注册到 `EntryProviderScope<NavKey>`。
- ViewModel 通过 Hilt 注入 `Repository`、`UserState` 或其他状态持有者；网络、数据库和本地 Store 访问分别由 `core/network/datasource/`、`core/database/datasource/`、`core/datastore/datasource/` 经 `core/data/repository/` 封装。当前本地 Store 的底层实现为 MMKV。
- View 复用 `core/ui/` 与 `core/designsystem/` 的组件、主题和状态容器。
- 业务页面跳转由 ViewModel 调用模块 Navigator，结果回传由 ViewModel 使用 `core.navigation` 提供的结果 API。页面顶部栏的普通返回操作由 Screen 直接调用 `navigateBack()`；其他业务导航不在 UI 中执行。

## 推荐阅读顺序

1. 先阅读[目录与命名规范](./structure.md)，确认文件放置位置。
2. 根据页面复杂度选择[View 规范](./view.md)中的 Route、Screen、Content 拆分方式。
3. 使用[ViewModel 规范](./viewmodel.md)组织 `StateFlow`、Repository 和副作用。
4. 按[创建页面流程](./create-page.md)完成 Route、ViewModel、View、Graph 与模块 Navigator 的接入。
5. 需要复制代码时参考[页面模板](./templates.md)，并将占位类型替换为真实模型。

阅读模板前，先确认页面属于单对象请求、分页列表、本地存储还是纯本地状态；基类选择错误会让后续状态和 UI 结构全部偏离。

## 相关链接

- [屏幕适配](../框架核心/screen-adaptation.md)：了解 `ScreenAdaptDemo` 使用的窗口断点和响应式布局。
- [Jetpack Compose 导航](https://developer.android.com/develop/ui/compose/navigation)
- [Navigation 3 官方指南](https://developer.android.com/guide/navigation/navigation-3)
- [Hilt 与 Jetpack 集成](https://developer.android.com/training/dependency-injection/hilt-jetpack)
