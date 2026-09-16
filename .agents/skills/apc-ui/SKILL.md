---
name: apc-ui
description: 使用 AndroidProject-Compose 设计系统、Miuix 和 core/ui 实现或重构 Compose 页面与组件，覆盖主题语义、页面状态、响应式断点、安全区、深浅色和 Preview。用户要求编写 UI、修改布局、统一样式、实现组件或适配手机、折叠屏和平板时使用。
---

# APC UI

文中的 `core/...` 与 `feature/...` 均为不绑定包名的逻辑路径；在当前 Android 源码集下定位对应文件，不把现有 package 前缀写成固定目录。

## 必读资料

读取 `AGENTS.md` 获取仓库级限制，但不得用它代替本 Skill 的专项规则。新建或修改任何 Compose 布局前，依次读取：

1. `docs/android-project-compose/框架核心/designsystem.md`
2. `docs/android-project-compose/框架核心/theme.md`
3. `docs/android-project-compose/框架核心/ui.md`
4. `docs/android-project-compose/框架核心/miuix.md`
5. `docs/android-project-compose/框架核心/screen-adaptation.md`
6. `docs/android-project-compose/业务功能/view.md`

## 必读源码

文档只说明设计意图，真实可用能力以当前源码为准。开始写布局前必须检查：

1. `core/designsystem/theme/Color.kt`、`Size.kt`、`Shape.kt`、`TextStyles.kt`、`Theme.kt`：确认颜色语义、间距、圆角、文字层级和 `MiuixTheme` 映射。
2. `core/designsystem/component/Box.kt`、`Column.kt`、`Row.kt`：扫描已有容器、对齐、填充、换行和内边距封装。
3. 按布局类型继续读取 `LazyList.kt`、`Scroll.kt`、`Spacer.kt`，核对列表、滚动和固定间距组件。
4. `core/ui/component/`：先检查目标场景对应的 AppBar、Scaffold、Text、ListItem、Loading、Empty、Network、Refresh、Skeleton 等实现。
5. 当前 Feature 的相邻页面和组件：确认项目实际组合方式、默认参数与事件回调风格。
6. `docs/miuix/components/index.md` 和目标 Miuix 组件源码：确认组件是否已存在、构造参数、默认值和状态行为。

相邻页面只用于了解当前调用方式，不自动视为规范模板。不能只根据函数名猜测行为；选中封装后继续读取它的函数体，确认默认 `fillMaxSize`、`fillMaxWidth`、padding、alignment、arrangement 和实际使用的设计令牌。

## 固定规则

- Feature 优先使用 `core/ui` 的语义组件，其次使用 `core/designsystem/component` 的布局封装，最后才考虑 Compose 基础组件。
- 已有封装能表达布局时，不直接使用 `Column`、`Row`、`Box`、`LazyColumn`、`LazyRow`、`Spacer` 或自行拼装相同 Modifier 链。
- 页面颜色优先读取 `AppColors` 或 `MiuixTheme.colorScheme`；特殊状态色必须来自 `Color.kt`。禁止在 Feature 中直接写 `Color(...)` 或十六进制颜色。
- 页面间距、padding 和列表 item spacing 必须来自 `Size.kt`。固定空隙优先使用 `SpaceVertical*()`、`SpaceHorizontal*()`，列表间距使用 `Arrangement.spacedBy(Space*)`，禁止重复写已有 `.dp` 数值。
- 文字优先检查 Miuix `Text`、`AppTextStyles` 和 `MiuixTheme.textStyles`，圆角优先使用 Miuix `Defaults` 或 `Shape.kt`，禁止重复写已有 `.sp`、`RoundedCornerShape(...)`。
- 现有令牌或封装无法表达真实设计时，先判断能力应进入 `core/designsystem`、`core/ui` 还是 Feature 私有组件；新增语义能力后再由页面使用，不在页面留下孤立魔法值。
- 只有现有封装确实无法满足布局行为时才使用 Compose 基础组件，并在代码附近说明不能复用的具体原因；不得为了少写参数绕过项目封装。
- 封装名称、文档和函数体使用的令牌不一致时，将其作为公共实现缺陷处理：修复任务中应从封装源头修正并检查调用方，审查任务中应报告证据；不得在 Feature 中用硬编码绕开。
- 项目没有等价封装的 Miuix 交互组件可以直接使用，但颜色、文字、Shape 和间距仍必须接入设计系统；不得用基础容器和 `clickable` 重新拼装 Button、Card、Preference 等已有 Miuix 交互语义。
- Feature 保留任一基础布局组件或魔法值时，必须在实施说明和交付结果中列出“现有封装无法表达”的具体证据；一句笼统的“无法满足”不能作为绕过依据。

## 固定检查顺序

1. 分析页面信息层级、交互状态、窗口宽度、方向和安全区需求。
2. 先完成必读源码检查，列出能够复用的 UI 组件、布局封装、颜色、间距、文字和圆角令牌。
3. 使用 Miuix、`core/ui` 和 `core/designsystem` 完成布局，不在 Feature 重复封装公共能力。
4. 保持 Route → Screen → Content：Route 收集状态，Screen 组合骨架与缺省状态，Content 绘制成功业务内容。
5. 将业务判断和状态更新通过回调交给 ViewModel，避免在点击 lambda 与 Composable 中堆叠业务逻辑。
6. 使用 `bp()`、`isXS()`、`isSM()`、`isMD()`、`isLG()` 处理窗口适配，不把断点缓存到 ViewModel。
7. 补充页面或组件 Preview，检查浅色、深色和任务涉及的手机、折叠屏、平板窗口。

## 禁止事项

- 不在尚未检查设计系统源码时直接开始写布局。
- 不硬编码重复颜色、字号、圆角和间距，不复制已有设计系统或公共 UI 组件。
- 不把 Loading、Empty、Error 或分页状态放进 Content。
- 不在 Screen/Content 获取 ViewModel、Repository 或 DataSource。
- 不使用固定设备型号代替当前窗口宽度判断，不假设平板始终全屏。
- 不为单次样式需求新增跨 Feature 通用组件或扩展。

## 验证

提交前逐项搜索本次 Feature 新增的 `Color(`、`.dp`、`.sp`、`RoundedCornerShape(` 以及基础 `Column`、`Row`、`Box`、`LazyColumn`、`LazyRow`、`Spacer` import；逐处对照真实源码，确认没有可复用令牌或封装，并在交付中列出必须保留的基础组件及原因。运行相关 Compose UI 测试和 Preview 检查，确认没有裁切、重叠或滚动冲突。至少验证浅色、深色和任务涉及的关键宽度；涉及窗口切换时验证状态不会因重组丢失。执行 `./gradlew :app:assembleDebug`、必要的 `./gradlew :app:lint` 与 `git diff --check`。
