---
name: apc-audit
description: 审查 AndroidProject-Compose 项目或指定功能域的架构边界、Route-Screen-Content 分层、Kotlin 注释、设计系统、数据链路、Navigation 3、Preview、测试和 Gradle 配置。用户要求全盘扫描、代码审查、架构检查、注释检查、预览完整性检查或质量报告时使用。
---

# APC Audit

文中的 `core/...` 与 `feature/...` 均为不绑定包名的逻辑路径；在当前 Android 源码集下定位对应文件，不把现有 package 前缀写成固定目录。

## 审查原则

默认只读取、诊断和报告；只有用户明确要求修复时才修改代码。读取 `AGENTS.md` 与 `docs/android-project-compose/README.md`，但不得用它们代替本 Skill 的审查维度。再按发现的问题加载对应专题，不一次读取所有文档。

使用结构化代码索引确认声明、调用和影响范围；使用文本搜索检查注释、硬编码、注解、资源和路径。不要只凭文件名、正则命中或诊断数量断定问题。

## 必读文档

根据审查目标先读取对应规范文档，再对照源码；文档不是背景资料，而是审查结论的规则来源：

- 页面布局与分层：`docs/android-project-compose/业务功能/view.md`、`docs/android-project-compose/业务功能/structure.md`。
- ViewModel 与代码规范：`docs/android-project-compose/业务功能/viewmodel.md`，涉及完整接入时同时读取 `docs/android-project-compose/业务功能/create-page.md`。
- 页面模板与新增页面：`docs/android-project-compose/业务功能/templates.md`、`docs/android-project-compose/业务功能/create-page.md`。
- 设计系统与屏幕适配：`docs/android-project-compose/框架核心/designsystem.md`、`theme.md`、`ui.md`、`screen-adaptation.md`。
- 导航：`docs/android-project-compose/导航/index.md`、`router.md`、`flow.md`、`guard.md`、`result.md`。
- 数据和状态：按问题读取 `框架核心/model.md`、`result.md`、`network.md`、`network-base.md`、`pagination.md`、`data.md`、`database.md`、`datastore.md`、`state.md`。
- Preview：`框架核心/annotation.md`、`data.md#预览数据` 和 `业务功能/view.md#预览规范`。

执行审查时，必须把“文档规定的职责”和“源码当前行为”逐项对照；发现文档与源码不一致时分别报告事实差异和影响，不以其中一方替代另一方。

## 必读源码

先读取被审查功能域的 Route、Screen、Content、ViewModel、Graph、Navigator、Repository 和测试，再按问题类型读取公共实现：

- UI：`core/designsystem/theme/`、`core/designsystem/component/` 与相关 `core/ui/component/` 源码。
- 数据：相关 Model、Service、DataSource、Repository、基础 ViewModel、状态和 Result 实现。
- 导航：`AppNavHost.kt`、`AppNavigator.kt`、`NavigationService.kt`、回退栈控制器与目标功能域路由文件。
- Preview：`core/annotation/`、Preview Provider 与页面 Preview 函数。
- 构建：Gradle、版本目录、Manifest、资源、ProGuard 与测试配置。

审查设计系统复用时，不能只统计 `.dp`、`Color(` 或基础布局 import。必须对照实际封装的签名和函数体，判断现有颜色、间距、Shape、Text、Box、Column、Row、List、Scroll、Spacer 或公共 UI 是否已经覆盖需求。

## 审查入口矩阵

| 审查目标 | 先读的规范 | 再查的源码 | 重点结论 |
| --- | --- | --- | --- |
| 布局与 View | `业务功能/view.md`、`框架核心/miuix.md`、`designsystem.md`、`theme.md`、`ui.md`、`screen-adaptation.md` | 目标 `Screen.kt`、`core/designsystem`、相关 `core/ui` | 是否复用 Miuix 组件、颜色、间距、Shape、TextStyles、Box/Column/Row/List/Scroll/Spacer 和状态组件 |
| ViewModel 与代码 | `业务功能/viewmodel.md`、`structure.md` | 目标 `ViewModel.kt`、Route、Repository、测试 | StateFlow 封装、状态注释、参数说明、基类选择、业务逻辑边界和失败处理 |
| 页面模板与新增页面 | `业务功能/templates.md`、`create-page.md`、`structure.md` | Routes、Navigator、Graph、Route、Screen、Content、ViewModel | 模板是否完整、是否按 Route → ViewModel → View → Graph → Navigator 接入，是否遗漏层级 |
| 导航 | `导航/index.md`、`router.md`、`flow.md`、`guard.md`、`result.md`、`业务功能/create-page.md` | `AppNavHost`、Navigator、Graph、回退栈、拦截器和结果 Key | 路由注册、模块 Navigator、参数、结果、登录拦截和回退行为 |
| 数据与网络状态 | 对应 Core 数据章节、`业务功能/viewmodel.md` | Model、Service、DataSource、Repository、基类 ViewModel 和状态容器 | 数据链路、成功/失败/空值、分页、刷新、重试和并发 |
| Preview | `框架核心/annotation.md`、`data.md#预览数据`、`业务功能/view.md#预览规范` | 注解、Provider、Screen Preview 和公共 UI | 静态数据、深浅色、多设备、状态覆盖和外部请求风险 |

## 固定规则

- 先给证据再下结论；命中规则只代表候选问题，真实职责和调用链决定最终严重程度。
- 页面必须满足 Route → Screen → Content，`MainScreen` 仅是无 `Scaffold` 特例。
- Feature 不绕过 Repository，不直接依赖其他 Feature 内部实现，不复制 Core 已有能力。
- UI 优先复用设计系统和公共组件；存在语义令牌或布局封装时，Feature 中的同义硬编码和基础布局属于需要修复的问题。
- 设计系统封装的名称、文档、默认参数和函数体不一致时，按公共实现缺陷记录，不建议业务页面用局部硬编码规避。
- 相邻页面、Demo 和公共组件都必须按同一规则核对，不能因为代码已存在或已经位于 Core 就默认判定为合规。
- 注释必须描述最终职责；Route 状态收集、字段、方法和关键分支必须可独立理解。
- 默认审查不授权修改；修复模式也只处理用户指定范围。

## 审查维度

1. 架构：`core/` 与 `feature/` 的依赖方向、Feature 私有与跨 Feature 能力的放置边界、单模块与 Gradle 模块的真实边界。
2. 页面：每个普通页面是否保留 Route → Screen → Content；Route 是否只收集状态和绑定事件；Screen 是否负责骨架与缺省状态；Content 是否只绘制最终业务内容。
3. 代码：是否符合 `业务功能/view.md` 和 `viewmodel.md` 的 Route、Screen、Content、ViewModel、StateFlow、参数注释和事件回调规范。
4. 状态：ViewModel 是否公开只读 `StateFlow`、是否通过 Repository 访问数据、View 是否把业务操作回调给 ViewModel。
5. UI：根据 `designsystem.md`、`theme.md`、`ui.md` 和 `screen-adaptation.md` 审查设计令牌、公共组件、硬编码、深浅色、窗口断点、安全区和重组范围。
6. 数据：Model、Service、DataSource、Repository、Result、Room、本地存储、全局状态和分页链路。
7. 导航：根据 `templates.md`、`create-page.md` 及导航专题审查类型安全 `NavKey`、模块 Navigator、Graph、参数、结果、登录拦截、路由唯一性和回退栈行为。
8. Preview：页面与组件注解、`PreviewParameterProvider`、静态数据、真实外部请求风险和多设备覆盖。
9. 代码质量：命名、中文 KDoc、状态变量注释、异常处理、测试、格式和静态检查。
10. 构建与资源：版本目录、SDK、BuildConfig、签名安全、混淆、ABI、图标、启动页和发布产物。

## 输出格式

- 按严重级别和影响范围排序问题，先给结论，再给文件、行号、证据和建议。
- 区分确定问题、潜在风险和文档差异；证据不足时继续调查，不写猜测性结论。
- 明确列出已检查范围、未检查范围、执行的命令和失败原因。
- 没有问题时直接说明未发现问题，不为填充报告制造建议。

## 修复模式

用户要求修复时按功能域或问题类型分批实施，保留工作区已有修改。每批运行最小相关验证，最后按风险执行 `./gradlew :app:assembleDebug`、`./gradlew :app:testDebugUnitTest`、必要的 `./gradlew :app:lint` 与 `git diff --check`，不顺带修复任务外问题。
