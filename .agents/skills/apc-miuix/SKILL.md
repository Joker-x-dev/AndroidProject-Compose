---
name: apc-miuix
description: 查阅和使用 AndroidProject-Compose 当前 Miuix Compose 组件库，覆盖组件选择、API 核对、Preference、Overlay、Navigation、PullToRefresh、Blur、Squircle 和项目封装边界。用户要求使用或审查 Miuix 组件时使用。
---

# APC Miuix

文中的 `core/...` 与 `feature/...` 均为不绑定包名的逻辑路径；当前源码中的真实 package、版本和函数签名优先于示例文字。

## 必读资料

读取 `AGENTS.md` 获取仓库级限制，再依次读取：

1. `docs/android-project-compose/框架核心/miuix.md`
2. `docs/miuix/components/index.md`
3. `docs/android-project-compose/框架核心/designsystem.md`
4. `docs/android-project-compose/框架核心/theme.md`
5. `docs/android-project-compose/框架核心/ui.md`

组件参数仍需核对当前 Miuix 依赖源码和同类页面，文档索引不替代源码验证。

## 必读源码

- `core/designsystem/theme/Theme.kt`、`Color.kt`、`TextStyles.kt`、`Shape.kt`、`Size.kt`：确认主题、令牌和 Squircle 开关。
- `core/ui/component/scaffold/AppScaffold.kt`、`MiuixPageLazyColumn.kt`：确认页面骨架、Insets、顶部栏、模糊和滚动行为。
- `core/ui/component/refresh/`、`network/`、`empty/`、`loading/`、`skeleton/`：确认项目级状态封装，避免重复实现。
- 目标 Feature 的相邻页面：确认 `top.yukonga.miuix.kmp.*` 的真实导入、默认参数和回调组合。
- `gradle/libs.versions.toml` 与 `app/build.gradle.kts`：确认 Miuix 依赖和版本。

## 固定规则

- 先在 `docs/miuix/components/index.md` 确认是否已有官方组件，再核对当前源码的构造参数、默认值、状态和约束。
- 通用交互优先直接使用 Miuix `Text`、`Button`、`Card`、`TopAppBar`、`Scaffold`、Preference、Navigation、Overlay 和 PullToRefresh。
- 页面骨架优先使用 `AppScaffold` 与 `MiuixPageLazyColumn`；网络、刷新、空态和加载优先使用 `core/ui` 封装。
- 颜色使用 `AppColors` 或 `MiuixTheme.colorScheme`，文字使用 `AppTextStyles` 或 `MiuixTheme.textStyles`，尺寸使用 `Size.kt`，自定义容器圆角使用 `Shape.kt`。
- Preference、Overlay 弹层和 Popup 组件必须确认是否需要在 Miuix `Scaffold` 环境中使用；不得仅根据名称猜测宿主要求。
- Blur 和 Squircle 必须尊重 `AppScaffold`、`LocalSquircleEnabled`、应用设置和运行时 Shader 能力，不在 Feature 复制模糊实现。
- Composable 只接收数据和事件回调，不获取 ViewModel、Repository、DataSource 或导航实例。

## 工作流

1. 根据需求确定目标组件分类和页面语义。
2. 查阅组件索引与具体组件文档，记录候选 Miuix API。
3. 在源码和相邻页面中确认导入、参数、默认行为、Insets、主题入口和宿主要求。
4. 判断能力应直接使用 Miuix、进入 `core/designsystem`、进入 `core/ui`，还是留在 Feature。
5. 组合实现并补充浅色、深色和必要窗口 Preview；Preview 使用静态数据，不发起真实请求。

## 验证

至少检查浅色、深色、窄屏和目标交互状态；涉及 Preference、Overlay、刷新、Blur 或 Squircle 时验证对应宿主和设置开关。运行受影响测试、`./gradlew :app:assembleDebug` 和 `git diff --check`。
