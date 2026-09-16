---
name: apc-feature
description: 创建、修改和重构 AndroidProject-Compose 业务功能域与 Compose 页面，覆盖目录、Route-Screen-Content、ViewModel、StateFlow、Graph、模块 Navigator、按需组件和 Preview。用户要求新增 Feature、页面、模块目录、页面模板或调整页面分层时使用。
---

# APC Feature

文中的 `core/...` 与 `feature/...` 均为不绑定包名的逻辑路径；在当前 Android 源码集下定位对应文件，不把现有 package 前缀写成固定目录。

## 必读资料

读取 `AGENTS.md` 获取仓库级限制，但不得用它代替本 Skill 的 Feature 分层规则。按任务读取：

- `docs/android-project-compose/业务功能/index.md`
- `docs/android-project-compose/业务功能/structure.md`
- `docs/android-project-compose/业务功能/view.md`
- `docs/android-project-compose/业务功能/viewmodel.md`
- `docs/android-project-compose/业务功能/create-page.md`
- 需要生成完整骨架时读取 `docs/android-project-compose/业务功能/templates.md`

涉及 UI、数据、导航或预览时继续遵守对应 APC Skill 与项目文档；即使没有显式组合其他 Skill，也必须执行下面的固定规则。

## 必读源码

创建或重构页面前，至少读取一组与目标数据形态一致的真实实现：

- 普通页面：相邻功能域的 `*Screen.kt` 与 `*ViewModel.kt`。
- 非分页网络页：`feature/demo/view/NetworkDemoScreen.kt`、`feature/demo/viewmodel/NetworkDemoViewModel.kt`。
- 分页列表页：`feature/demo/view/NetworkListDemoScreen.kt`、`feature/demo/viewmodel/NetworkListDemoViewModel.kt`。
- 顶级容器：`feature/main/view/MainScreen.kt`，只用于理解无 `Scaffold` 的特例，不把它复制到普通页面。
- 页面注册：目标功能域的 `*Graph.kt`、`core/navigation/<domain>/*Routes.kt` 与 `*Navigator.kt`。
- 页面布局：先扫描 `core/designsystem/theme/Color.kt`、`Size.kt`、`Shape.kt`、`TextStyles.kt` 和 `core/designsystem/component/` 的真实声明，再完整读取准备使用的 Miuix、布局封装与 `core/ui` 组件。

相邻页面和 Demo 只用于确认当前调用链，不自动视为规范模板。复制前必须按本 Skill 与 `apc-ui` 的固定规则检查其分层、设计令牌、布局封装和注释；已有页面不合规时不能继续传播。

## 固定规则

- 每个普通页面都保留 Route → Screen → Content；`MainScreen` 仅免除 `Scaffold`，不免除三层。
- Route 只获取 ViewModel、收集状态和绑定事件；每个 `collectAsState()` 上方写明具体状态含义。
- Screen 只负责页面骨架、AppBar 和 Loading、Empty、Error；成功布局必须进入 Content。
- Content 不获取 ViewModel、Repository、DataSource 或导航对象，只接收可渲染数据和回调。
- 业务判断、状态更新、Repository 调用和业务导航进入 ViewModel；普通返回在 Screen 直接调用 `navigateBack()`。
- 页面布局必须优先复用 `core/ui` 与 `core/designsystem`。已有语义颜色、间距、Shape、文字和 Box/Column/Row/List/Scroll/Spacer 封装时，不写局部魔法值或同义基础布局。
- 需要保留 Compose/Material 基础组件时，必须确认项目没有等价封装，并在实施说明中记录具体原因；视觉参数仍使用设计系统。
- Feature 只依赖 Core 和公开导航契约，不直接依赖其他 Feature 的页面、ViewModel 或内部组件。

## 工作流

1. 先确认需求应扩展已有 Demo/功能域还是建立正式业务域，再检查相邻页面、路由、Navigator、数据链路和测试；已有同义能力优先复用，不复制第二套实现。
2. 根据页面数据形态选择普通页面、非分页网络页或分页列表页，复用现有基类与状态容器。
3. 按固定规则实现 Route、Screen、Content 与 ViewModel，不在工作流阶段重新解释或放宽各层职责。
4. 按真实复杂度创建 `component/`、`state/`、`model/`、`data/`、`skeleton/`、`base/` 或 `extension/`，不为目录完整创建空文件，也不新增项目尚未约定且没有明确职责的抽象目录。
5. 接入类型安全 Route、Feature Graph、模块 Navigator 和 Preview，并补充对应测试。

## 注释与边界

- 为主要声明、构造参数、字段、状态、公开与私有方法添加可独立理解的中文 KDoc；关键局部变量使用中文行内注释。
- 注释只描述最终职责、业务含义和设计原因，不记录修改过程、协作对话或人称表达。
- 页面事件优先使用 `viewModel::method` 从 Route 绑定，避免在 View 中堆叠业务逻辑。
- Feature 不直接依赖其他 Feature 的 View、ViewModel 或内部组件；跨功能域跳转调用目标模块 Navigator。

## 验证

运行受影响的单元测试、Compose Preview 检查、`./gradlew :app:assembleDebug` 和 `git diff --check`。确认三层职责、状态收集注释、导航注册和成功布局入口完整，并逐处核对新增布局没有绕过可复用的设计令牌、布局封装和公共 UI。
