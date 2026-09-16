# Core 核心能力

## 介绍

`core/` 是 AndroidProject-Compose 的基础能力层。它和 `feature/` 一起编译在唯一的 `:app` Gradle 模块中，通过包路径区分职责，而不是通过 Gradle 依赖形成编译隔离。

如果把一个页面看成“展示状态并响应操作”，Core 就负责提供页面需要的状态基类、数据访问入口、主题、通用组件和导航运行时。Feature 只负责把这些能力组合成具体业务页面。

## 设计目标

- **统一入口**：网络、Room 和本地存储都通过 Repository 进入 Feature，页面不直接创建 Service、DAO 或存储实例。
- **统一状态**：单对象请求和分页列表分别使用对应网络基类，页面不重复实现 Loading、Error 和重试逻辑。
- **统一视觉**：颜色、字体、圆角、间距和基础布局由 Design System 提供，避免页面散落魔法数。
- **统一导航**：路由、回退栈、登录拦截和结果回传由 `core/navigation` 管理。
- **明确示例边界**：`core/state` 同时包含用户状态和 Demo 计数状态，`core/model` 也包含项目示例业务模型；示例模型不代表所有业务都必须采用相同结构。

## 模块组成

| 目录 | 负责什么 | 初学者先看什么 |
| --- | --- | --- |
| `base/` | `BaseViewModel`、网络请求基类和 UI 状态 | [ViewModel 基类](./base.md) |
| `annotation/` | 项目级通用注解；当前已实现 Compose 预览注解 | [注解](./annotation.md) |
| `designsystem/` | Miuix 颜色、TextStyles、圆角、尺寸和布局封装 | [Miuix Compose](./miuix.md)、[设计系统](./designsystem.md)、[主题系统](./theme.md) |
| `ui/` | Scaffold、AppBar、网络容器、空态、加载态和窗口断点 | [UI 组件](./ui.md)、[屏幕适配](./screen-adaptation.md) |
| `model/` | 网络响应、分页、请求、实体和通用 ID 模型 | [数据模型](./model.md) |
| `result/` | Flow 结果转换、业务成功码和错误回调 | [请求结果处理](./result.md) |
| `network/` | Retrofit Service、DataSource、拦截器和依赖注入 | [网络请求](./network.md) |
| `data/` | Repository 与可复用的设计期预览数据 | [数据层](./data.md) |
| `database/` | Room Database、DAO 和数据库 DataSource | [Room 数据库](./database.md) |
| `datastore/`、`util/storage/` | 本地存储接口与实现；当前底层使用 MMKV | [本地存储](./datastore.md) |
| `state/` | `UserState` 和 `DemoCounterState` 等应用级状态 | [全局状态](./state.md) |
| `navigation/` | `NavKey`、Graph 宿主、导航器、拦截和结果 | [导航概览](../导航/index.md) |
| `util/` | Toast、权限、包信息和时间计算 | [工具类](./util.md) |

`core/extension/` 是按需创建的 Kotlin 扩展目录。只有扩展函数跨多个 Feature 复用、命名稳定且不依赖具体页面时才放入该目录；单个 Feature 私有的扩展函数保留在对应业务域。

## 一条完整的数据链路

以网络列表为例，代码不会从 Compose 直接跳到 Retrofit，而是沿着下面的方向流动：

```text
NetworkListDemoRoute
    ↓ 收集 StateFlow、转发刷新/重试事件
BaseNetWorkListViewModel<Goods>
    ↓ requestListData()
GoodsRepository
    ↓ getGoodsPage()
GoodsNetworkDataSource → GoodsService → Retrofit
    ↓ NetworkResponse<NetworkPageData<Goods>>
BaseNetWorkListViewModel 合并列表与分页状态
    ↓
BaseNetWorkListView → RefreshLayout → Compose 列表
```

这条链路里的每一层都有独立职责：Route 连接状态，ViewModel 管理请求和状态，Repository 选择数据源，DataSource 隔离实现，UI 组件负责展示状态。修改其中一层时，先回到对应章节确认它的边界。

## 推荐阅读顺序

第一次阅读建议按下面顺序进行，不要直接跳到页面模板：

1. [Miuix Compose](./miuix.md)、[设计系统](./designsystem.md)和[主题系统](./theme.md)：先了解组件库、页面颜色、文字、间距和基础布局。
2. [UI 组件](./ui.md)：了解 `AppScaffold`、网络容器、空态和刷新组件。
3. [注解](./annotation.md)和[屏幕适配](./screen-adaptation.md)：了解项目级注解边界，并学习当前已有的主题、多设备预览和窗口断点。
4. [ViewModel 基类](./base.md)：理解页面状态由谁持有。
5. [数据模型](./model.md)和[请求结果处理](./result.md)：理解网络返回值如何变成页面状态。
6. [网络请求](./network.md)和[数据层](./data.md)：理解请求从 Service 到 ViewModel 的完整路径，并按需组织复用预览数据。
7. [Room 数据库](./database.md)、[本地存储](./datastore.md)和[全局状态](./state.md)：学习本地数据和应用级状态。
8. [非分页网络基类](./network-base.md)和[分页列表](./pagination.md)：把基础能力组合到页面。
9. 最后阅读[Navigation](../导航/index.md)和[Feature](../业务功能/index.md)，创建自己的业务页面。

## Core 收录边界

一项能力只有在跨多个 Feature 复用、职责稳定且不依赖具体业务页面时，才属于 Core。只服务单个业务域的组件、状态和模型应留在对应 Feature；需要进入 Core 的能力还必须遵守现有依赖方向，不能让通用目录反向引用具体页面。

具体能力的接入和扩展方式由对应章节说明，例如主题令牌放在[主题系统](./theme.md)，公共组件放在 [UI 组件](./ui.md)，运行时数据入口和复用预览数据放在[数据层](./data.md)，项目级通用注解放在[注解](./annotation.md)。

::: warning 单模块边界
`core/` 不是独立 Gradle 模块。当前编译器不会阻止 Feature 直接调用 Core 内部实现，因此“页面只能经过 Repository、Content 不获取 ViewModel”等规则需要通过目录约定和代码审查维护。
:::

## 相关链接

- [项目架构与职责](../简介/architecture.md)：从应用启动到页面和数据源的整体链路。
- [工程组织与模块边界](../简介/modularization.md)：理解单 `:app` 模块下的包级分层。
- [Feature 模块概览](../业务功能/index.md)：把 Core 能力组合成业务页面。
- [Android 官方应用架构指南](https://developer.android.com/topic/architecture)
