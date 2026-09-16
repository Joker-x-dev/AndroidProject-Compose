# 目录与命名规范

## 介绍

Feature 目录按业务域组织，例如 `feature/auth/`、`feature/common/`、`feature/main/` 和 `feature/user/`。本页只说明什么时候拆分文件、文件放在哪里以及如何命名，不要求每个功能域复制完全相同的目录。

当前 AndroidProject-Compose 只有一个 `:app` Gradle 模块，Feature 只是包级分层。路由类型放在 `core/navigation/<domain>/`，页面实现放在 `feature/<domain>/`。

## 从一个页面文件开始

普通页面可以先使用下面的最小结构：

```text
feature/profile/
├── navigation/
│   └── ProfileGraph.kt
├── view/
│   └── ProfileScreen.kt
└── viewmodel/
    └── ProfileViewModel.kt
```

`ProfileScreen.kt` 包含 `ProfileRoute`、`ProfileScreen`、`ProfileContent` 和 Preview。三层函数可以先放在同一文件；只有子组件需要复用或职责已经独立时才继续拆分文件，不为尚不存在的代码创建空目录。

## 功能域可能包含的目录

功能域按实际职责选择目录，不要求复制完整结构。目录达到表中的创建条件时再新增。

| 目录 | 创建条件 | 负责内容 | 不负责内容 |
| --- | --- | --- | --- |
| `navigation/` | 功能域存在可导航页面 | Navigation 3 Graph，将 `NavKey` 映射到 Route | 页面布局、请求和业务状态 |
| `view/` | 存在 Compose 页面 | Route、Screen、Content 和页面 Preview | Repository 实现、数据源和持久化 |
| `viewmodel/` | 页面需要状态、请求或交互逻辑 | 页面状态入口、业务操作、Repository 调用和导航动作 | Compose 布局和底层 DataSource |
| `component/` | 功能域内多个页面复用业务 UI，或页面区域已经足够独立 | 卡片、弹层、列表项和业务组合组件 | 跨 Feature 通用组件和完整页面 |
| `state/` | 页面状态类型或状态转换已经不适合继续堆在 ViewModel | `UiState`、状态分支和直接服务该状态的转换代码 | 应用级共享状态和持久化数据 |
| `model/` | 功能域需要自己的 UI 模型、筛选项或辅助业务模型 | 只在当前功能域使用的数据结构 | 服务端通用实体、请求体和跨 Feature 模型 |
| `data/` | 功能域多个页面共享静态菜单、展示配置或 Preview 数据 | Feature 私有的静态展示数据、`PreviewParameterProvider` 和配置 | Repository、NetworkDataSource、DAO 和本地存储实现 |
| `skeleton/` | 页面需要与真实布局对应的加载骨架 | 功能域页面骨架和骨架组合 | 请求状态管理和通用 Loading 组件 |
| `base/` | 当前功能域内多个页面具有稳定、真实的共同父类或页面骨架 | 领域专用 Base ViewModel、公共 Tab Screen 等 | 只有一个调用方的提前抽象和跨 Feature 基类 |
| `extension/` | 当前功能域多个文件复用同一 Kotlin 扩展 | 具有明确业务语义的 Feature 私有扩展函数 | 跨 Feature 通用扩展和无明确归属的工具函数 |

只被一个页面使用的私有 Composable、简单状态和 Preview 数据可以继续留在页面文件中。同一 Feature 内多处复用时再拆到对应目录；跨 Feature 复用时再上移到 `core/` 对应目录。直接服务 `UiState` 的状态转换代码放在 `state/`。

## 文件命名

| 文件类型 | 文件名 | 主要符号 |
| --- | --- | --- |
| 页面 | `<PageName>Screen.kt` | `<PageName>Route`、`<PageName>Screen`、`<PageName>Content` |
| ViewModel | `<PageName>ViewModel.kt` | `<PageName>ViewModel` |
| Graph | `<Domain>Graph.kt` | `<domain>Graph()` |
| 路由 | `<Domain>Routes.kt` | `<Domain>Routes` |
| Navigator | `<Domain>Navigator.kt` | `<Domain>Navigator` |
| 组件 | 描述具体 UI 结构 | 例如 `HomeSubTabSection.kt` |
| UI 状态 | `<Name>UiState.kt` | 例如 `HomeSecondFloorUiState.kt` |
| 骨架屏 | `<PageName>Skeleton.kt` | 例如 `HomeTabSkeleton.kt` |
| Preview Provider | `<Name>PreviewParameterProvider.kt` | 例如 `MessageTypeVoPreviewParameterProvider.kt` |
| 扩展函数 | `<DomainOrType>Ext.kt` 或具体职责名 | 例如 `OrderStatusExt.kt` |

Kotlin 文件使用 PascalCase，包名使用全小写英文。避免 `Common.kt`、`Utils.kt`、`Manager.kt`、`Data.kt` 这类无法从文件名判断职责的名称。

## 拆分为 Gradle 模块后

当前 `feature/<domain>/` 只是 `:app` 内的包目录。功能域需要独立编译、限制依赖或被多个应用复用时，可以进一步拆成公开契约与内部实现：

```text
feature/<domain>/
├── api/                            # 对其他模块公开
│   ├── build.gradle.kts
│   └── src/main/.../
│       ├── <Domain>Routes.kt       # 类型安全路由契约
│       └── <Domain>Navigator.kt    # 对外跳转入口
└── impl/                           # 仅当前功能域内部使用
    ├── build.gradle.kts
    └── src/main/.../
        ├── navigation/
        ├── view/
        ├── viewmodel/
        └── 其他按需目录
```

其他模块只依赖 `api`，应用模块负责依赖并组装 `impl`。这样可以隐藏页面、ViewModel 和内部组件，只暴露稳定的路由与导航入口。这种公开契约与内部实现分离适合大型 Android 工程，也便于逐步演进为 Now in Android 一类的多模块结构。

只有真正创建独立 Gradle 模块后才使用 `api/impl`。当前单 `:app` 工程继续使用普通功能域目录，不能只增加文件夹名称却没有对应的 Gradle 依赖边界。

## 相关链接

- [Feature 模块概览](./index.md)
- [View 规范](./view.md)
- [ViewModel 规范](./viewmodel.md)
- [创建页面流程](./create-page.md)
- [工程组织与模块边界](../简介/modularization.md)
- [Android 官方模块化指南](https://developer.android.com/topic/modularization)
- [Now in Android](https://github.com/android/nowinandroid)
