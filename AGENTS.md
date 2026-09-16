# AndroidProject-Compose 项目规则

## 项目概览与技术栈

AndroidProject-Compose 是一个基于 Kotlin、Jetpack Compose 和 Navigation 3 的 Android 脚手架。项目把通用能力放在 `core/`，把业务页面放在 `feature/`，通过包级边界组织代码。

| 领域 | 技术与用途 |
| --- | --- |
| 语言与 UI | Kotlin、Jetpack Compose、Miuix Compose |
| 页面与状态 | Route → Screen → Content、ViewModel、StateFlow |
| 依赖注入 | Hilt |
| 网络与模型 | Retrofit、OkHttp、Kotlin Serialization |
| 数据访问 | Repository、NetworkDataSource、Room、本地存储接口 |
| 导航 | AndroidX Navigation 3、类型安全 `NavKey`、模块级 Navigator |
| 开发与质量 | Gradle、JVM 单元测试、Compose Preview |

## 执行原则与资料优先级

修改代码或文档前，先阅读任务对应的本地章节，再核对当前源码、调用方和测试。禁止根据类型名称、其他平台实现或记忆猜测 Android API。

资料优先级如下：

1. 用户当前任务与本文件的项目级约束。
2. [AndroidProject-Compose 框架文档](docs/android-project-compose/README.md)。
3. 当前源码、同类实现、调用关系和测试用例。
4. Android、Kotlin 和第三方依赖的官方文档。

文档与代码不一致时，以可运行源码和测试结果为依据，并同步更新项目内副本与在线文档。

## 按任务读取文档

| 开发任务 | 必读章节 |
| --- | --- |
| 项目架构与模块拆分 | [项目架构与职责](docs/android-project-compose/简介/architecture.md) → [工程组织与模块边界](docs/android-project-compose/简介/modularization.md) |
| 主题、布局与公共 UI | [Miuix Compose](docs/android-project-compose/框架核心/miuix.md) → [设计系统](docs/android-project-compose/框架核心/designsystem.md) → [主题系统](docs/android-project-compose/框架核心/theme.md) → [UI 组件](docs/android-project-compose/框架核心/ui.md) |
| Miuix 组件参数与组件选择 | [Miuix 组件索引](docs/miuix/components/index.md) → [Miuix Compose 接入规范](docs/android-project-compose/框架核心/miuix.md) |
| 编写 Route、Screen、Content | [Feature 概览](docs/android-project-compose/业务功能/index.md) → [View 规范](docs/android-project-compose/业务功能/view.md) |
| 编写 ViewModel 与页面状态 | [ViewModel 规范](docs/android-project-compose/业务功能/viewmodel.md) → [ViewModel 基类](docs/android-project-compose/框架核心/base.md) |
| 接入网络、数据与分页 | [数据模型](docs/android-project-compose/框架核心/model.md) → [请求结果处理](docs/android-project-compose/框架核心/result.md) → [网络请求](docs/android-project-compose/框架核心/network.md) → [数据层](docs/android-project-compose/框架核心/data.md) → [非分页网络基类](docs/android-project-compose/框架核心/network-base.md)或[分页列表](docs/android-project-compose/框架核心/pagination.md) |
| 使用 Room、本地存储或全局状态 | [数据层](docs/android-project-compose/框架核心/data.md) → [Room 数据库](docs/android-project-compose/框架核心/database.md)、[本地存储](docs/android-project-compose/框架核心/datastore.md)或[全局状态](docs/android-project-compose/框架核心/state.md) |
| 配置路由、参数、拦截与结果 | [导航概览](docs/android-project-compose/导航/index.md) → [路由配置](docs/android-project-compose/导航/router.md) → [导航流程](docs/android-project-compose/导航/flow.md) → [登录与路由拦截](docs/android-project-compose/导航/guard.md) → [参数传递与结果回传](docs/android-project-compose/导航/result.md) |
| 创建完整 Feature 页面 | [目录与命名规范](docs/android-project-compose/业务功能/structure.md) → [创建页面流程](docs/android-project-compose/业务功能/create-page.md) → [页面模板](docs/android-project-compose/业务功能/templates.md) |
| 配置 Preview 与屏幕适配 | [注解](docs/android-project-compose/框架核心/annotation.md) → [数据层的预览数据](docs/android-project-compose/框架核心/data.md#预览数据) → [屏幕适配](docs/android-project-compose/框架核心/screen-adaptation.md) → [View 预览规范](docs/android-project-compose/业务功能/view.md#预览规范) |

## 架构与放置边界

- `core/` 提供跨 Feature 复用的基础能力；`feature/` 承载业务页面。通用层不得反向依赖具体 Feature。文档中的目录均为逻辑目录，不绑定源码包名或本地文件系统路径。
- `core/data/` 不只是 Repository 目录，也承载跨 Feature 复用的 Preview 数据；单个 Feature 专用的静态数据和 `PreviewParameterProvider` 留在该 Feature 的 `data/`。
- `core/annotation/` 用于项目通用注解。当前包含页面预览、组件预览和多设备预览注解；新增通用注解仍放入该目录。
- `core/extension/` 是按需创建的 Kotlin 扩展位置。跨多个 Feature 复用且具有明确语义的扩展才提升到 Core，Feature 私有扩展保留在业务域。
- 网络、数据库和本地存储都通过 Repository 进入页面；ViewModel 不直接创建 Service、DAO 或具体存储实例。
- `core/navigation/` 声明类型安全路由、模块 Navigator 和导航运行时；`feature/<domain>/navigation/` 只注册本功能域 Graph。
- 当前工程为单 `:app` 模块。只有当功能域需要独立编译、独立依赖或独立发布时，才按[模块化设计](docs/android-project-compose/简介/modularization.md)拆分 Gradle 模块。
- 文档中的文件位置使用 `core/`、`feature/<domain>/` 等逻辑目录；代码示例里的 `package` 和 `import` 只代表当前源码基线，包名迁移使用 Android Studio 重构同步更新。

## 页面与状态规范

- 每个普通页面都使用 `${PAGE_NAME}Route` → `${PAGE_NAME}Screen` → `${PAGE_NAME}Content` 三层。`MainScreen` 是顶级页面容器的特殊实现，不使用 `Scaffold`，但仍保留对应的 Route、Screen 和 Content 层。
- Route 只负责注入 ViewModel、收集公开只读 `StateFlow`、转发事件，并在每个状态变量旁说明状态含义。典型写法是 `val uiState by viewModel.uiState.collectAsState()`。
- Screen 负责 `Scaffold`、AppBar、页面骨架和 Loading、Empty、Error 等缺省状态；成功状态下的最终业务布局必须交给 Content。
- Content 只接收可渲染数据与事件回调，不直接访问 ViewModel、Repository、DataSource 或导航实现。
- 页面中的大部分业务逻辑通过 View 回调进入 ViewModel；页面顶部栏的普通返回操作直接调用 `navigateBack()`，不为它额外创建只转发返回的方法。
- ViewModel 负责状态、请求、Repository 调用和导航副作用；View 不负责业务判断、网络请求或持久化。
- 完整页面至少提供 `@ScreenPreview` 与 `@ScreenPreviewDark`；组件使用 `@ComponentPreview`、`@ComponentPreviewDark` 或组合注解。复杂页面通过 `@PreviewParameter` 注入静态预览数据，Preview 不发起真实请求。

## UI、主题与 Miuix

- 所有业务 UI 优先组合 Miuix 官方组件，并使用 `AppColors`、`AppTextStyles`、`Size.kt`、`Shape.kt` 和现有 `core/ui` 封装；禁止硬编码颜色、间距、圆角或重复实现 Miuix 组件。
- 新建或修改布局前必须阅读 [Miuix Compose 接入规范](docs/android-project-compose/框架核心/miuix.md)、[设计系统](docs/android-project-compose/框架核心/designsystem.md)、[主题系统](docs/android-project-compose/框架核心/theme.md) 和 [UI 组件](docs/android-project-compose/框架核心/ui.md)。
- Miuix 组件参数先查阅 [Miuix 组件索引](docs/miuix/components/index.md) 及对应组件页面，再核对当前依赖源码；不得根据其他平台组件名称或记忆猜测 Kotlin API。
- Miuix 组件运行在 `AppTheme` 提供的 `MiuixTheme` 组合环境中。页面优先读取 `AppColors`、`AppTextStyles` 或 `MiuixTheme`，不能创建局部主题对象。
- `AppScaffold`、`MiuixPageLazyColumn`、`RefreshLayout`、网络状态容器和空态组件属于项目级页面语义封装；通用 Button、Text、Card、Preference、Navigation 和弹窗优先直接使用 Miuix 官方实现。

### Miuix 组件索引

写 UI 前至少扫描对应分类，组件的完整说明和参数以 [docs/miuix/components/index.md](docs/miuix/components/index.md) 及具体组件文档为准。

| 分类 | 组件列表 |
| --- | --- |
| 脚手架 | Scaffold |
| 基础容器与导航 | Surface、TopAppBar、NavigationBar、NavigationRail、TabRow、BreadcrumbBar、Card、BasicComponent |
| 基础操作 | Button、IconButton、Text、SmallTitle、TextField、Switch、Checkbox、RadioButton、Slider、NumberPicker |
| 状态与反馈 | ProgressIndicator、Snackbar、Tooltip、Badge、Icon、FloatingActionButton、FloatingToolbar、Divider、PullToRefresh |
| 搜索与颜色 | SearchBar、ColorPalette、ColorPicker |
| Preference | ArrowPreference、SwitchPreference、CheckboxPreference、RadioButtonPreference、SliderPreference、RangeSliderPreference |
| Overlay 弹层 | OverlayListPopup、OverlayCascadingListPopup、OverlayDropdownPreference、OverlaySpinnerPreference、OverlayDropdownMenu、OverlayIconDropdownMenu、OverlayIconCascadingDropdownMenu、OverlayBottomSheet、OverlayDialog |
| Window 弹层 | WindowListPopup、WindowCascadingListPopup、WindowDropdownPreference、WindowSpinnerPreference、WindowDropdownMenu、WindowIconDropdownMenu、WindowIconCascadingDropdownMenu、WindowBottomSheet、WindowDialog |

### UI 开发固定检查顺序

1. 在 Miuix 组件索引中确认是否已有对应组件，优先组合官方 Miuix API。
2. 在 `core/designsystem` 和 `core/ui` 中确认是否已有主题令牌、布局封装或页面状态容器。
3. 查阅具体组件文档和当前依赖源码，确认构造参数、默认值、回调类型、Insets 和状态行为。
4. 使用 Miuix 主题语义完成组合，再补充浅色、深色和必要的窗口 Preview。

## Kotlin、命名与注释

- 遵循 Kotlin 官方编码规范。文件使用 `PascalCase.kt` 并与主要类型同名，类型使用 `PascalCase`，变量和方法使用 `camelCase`，常量使用 `UPPER_SNAKE_CASE`，包名使用全小写英文。
- import 下方、主要声明前添加中文 KDoc，说明文件或类型职责。类型、构造参数、字段、状态、公开与私有方法均使用 KDoc；参数使用 `@param`，非 `Unit` 返回值使用 `@return`。
- Route 中每个 `collectAsState()`、关键状态转换、失败分支和不直观的布局计算必须添加中文行内注释。重写属性和重写方法仍需说明其业务含义，不能因为接口已有声明而省略。
- 注释只描述最终职责、业务含义和设计原因，不记录修改过程、协作对话或人称表达。
- 代码关键字与 API 保持英文，注释使用中文。避免 `Common.kt`、`Utils.kt`、`Manager.kt`、`Data.kt` 等无法表达领域职责的名称。

## 验证与交付

- 代码变更至少运行 `./gradlew assembleDebug` 和 `./gradlew testDebugUnitTest`；涉及静态检查时追加 `./gradlew lint`。
- 文档变更运行项目内链接检查，并在文档站仓库运行 `pnpm docs:build`；网站与本地副本的页面、图片和链接保持一致。
- 提交前运行 `git diff --check`，不得提交 `build/`、`.gradle/`、文档站 `dist/` 或缓存目录。
- 保留工作区中与任务无关的修改，不使用整体重置或覆盖方式清理文件。

## AI 参考入口

- 按任务选择 `.agents/skills/` 下的 `apc-*` 项目 Skill；Skill 负责执行流程，项目文档负责解释架构与能力，源码和测试负责确认当前行为。
- 优先阅读项目内的[框架文档](docs/android-project-compose/README.md)，其中包含架构、页面、数据、导航和 Preview 的完整说明。
- 项目内文档未覆盖的主题，可访问[在线文档](https://compose.dusksnow.top)查看与当前版本同步的章节。
