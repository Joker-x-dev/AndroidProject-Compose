---
title: "AI 辅助开发"
---

# AI 辅助开发

AndroidProject-Compose 将项目级规则、专项 Skill、框架文档、源码和测试作为一套完整的开发上下文。支持这些约定的 AI 工具可以先理解项目的页面分层、数据链路和设计系统，再执行代码生成、重构或审查，减少 API 猜测、目录误用和重复实现。

## Skill 快速索引

项目 Skill 位于 `.agents/skills/`，与 AndroidProject-Compose 源码一起维护。支持 Skill 的工具可以通过 `$apc-*` 选择工作流；如果工具不支持自动匹配，也可以直接要求它读取对应的 `SKILL.md`。

| Skill | 适用任务 | 调用示例 |
| --- | --- | --- |
| `apc-setup` | 修改应用名称、项目名、包名、`applicationId`、Logo、图标和启动页 | `使用 $apc-setup 初始化新的应用项目` |
| `apc-feature` | 创建或重构 Feature、Route、Screen、Content、ViewModel、Graph 和页面目录 | `使用 $apc-feature 创建商品详情页面` |
| `apc-ui` | 编写 Compose 布局、复用设计系统、公共组件、屏幕适配和安全区 | `使用 $apc-ui 重构个人中心布局` |
| `apc-miuix` | 查阅和使用 Miuix 组件、Preference、Overlay、Navigation、刷新、Blur 和 Squircle | `使用 $apc-miuix 接入设置页 Preference` |
| `apc-data` | 接入模型、网络、Repository、Room、本地存储、结果处理和分页 | `使用 $apc-data 接入商品分页接口` |
| `apc-nav` | 配置路由、模块级 Navigator、参数、结果回传和登录拦截 | `使用 $apc-nav 接入商品详情路由` |
| `apc-preview` | 编写页面与组件 Preview、静态预览数据和 `PreviewParameter` | `使用 $apc-preview 为列表页补充预览` |
| `apc-theme` | 修改颜色、字体、Shape、尺寸、深色模式和动态颜色 | `使用 $apc-theme 新增主题颜色` |
| `apc-build` | 维护 Gradle、SDK、构建类型、资源、签名、混淆和发布产物 | `使用 $apc-build 排查 Release 构建失败` |
| `apc-audit` | 审查架构、页面分层、注释、UI、数据、导航、Preview、测试和构建配置 | `使用 $apc-audit 审查 feature/main` |

一个需求可以组合多个 Skill。新增带网络、导航和预览的页面时，通常组合 `apc-feature`、`apc-data`、`apc-ui`、`apc-nav` 和 `apc-preview`；修改主题时组合 `apc-theme` 与 `apc-ui`；发布前使用 `apc-build` 和 `apc-audit`。

## AI 上下文由什么组成

下面的目录展示 AI 工具完成一次 Android 任务时应能够访问的主要上下文；目录只表示职责，不绑定具体包名。

```text
AndroidProject-Compose/
├── AGENTS.md                         # 项目级规则、资料优先级和验证要求
├── .agents/skills/                   # 按职责拆分的项目 Skill
├── docs/android-project-compose/     # 项目内框架文档
├── app/src/main/                     # 可运行源码与真实 API
├── app/src/test/                     # JVM 单元测试
└── gradle/                           # 版本目录与 Gradle 配置
```

项目内文档用于说明架构和能力，源码与测试用于确认具体版本的实现。文档说明职责边界，源码确认实际行为，两者需要结合阅读。

### `AGENTS.md`

`AGENTS.md` 是所有开发任务共享的项目级规则，定义资料优先级、`core/` 与 `feature/` 的边界、Route → Screen → Content 分层、设计系统复用、Kotlin 注释和构建验证要求。AI 开始修改前必须读取该文件，不能只根据一段需求直接生成代码。

### 项目 Skill

Skill 将通用规则收敛为某类任务的执行流程。例如：

- `apc-ui` 要求先读取 Miuix、设计系统、主题、UI、屏幕适配文档，并检查真实组件源码，不能凭记忆创建颜色、间距或布局 API。
- `apc-miuix` 要求先查阅 Miuix 组件索引，再核对当前依赖源码和相邻页面，确认组件参数、宿主要求和项目封装边界。
- `apc-data` 要求沿着 Model → NetworkDataSource → Repository → ViewModel 的链路工作，不能让页面直接访问网络、DAO 或本地存储实现。
- `apc-nav` 在创建导航页面时还要读取页面模板和创建页面流程，确认 Routes、Graph、Route、Screen、Content、ViewModel 和模块级 Navigator 都已接入。
- `apc-preview` 要求使用静态预览数据和 `PreviewParameter`，Preview 不发起真实网络请求。
- `apc-audit` 只负责审查和报告；读取页面模板是为了核对实现是否符合规范，不负责代替页面创建流程。

Skill 负责“如何执行任务”，项目文档负责解释“能力如何设计和使用”，源码与测试负责确认“当前版本实际如何运行”。三者不能互相替代。

## 推荐工作流

以下流程适用于代码新增、重构、问题排查和审查任务，步骤顺序可以根据任务规模合并，但资料读取和源码核对不能省略。

```text
明确目标与验收条件
        ↓
读取 AGENTS.md
        ↓
选择对应 apc-* Skill
        ↓
读取专项文档和相邻实现
        ↓
核对源码、调用关系和测试
        ↓
实施修改并运行相关验证
        ↓
检查差异并报告结果
```

### 明确任务边界

请求中应说明目标、范围、约束和完成条件。下面的示例描述一个完整的分页页面任务：

以下文本可以直接作为支持 Skill 的 AI 工具的任务输入，示例中的业务名称和验收条件需要替换为实际需求。

```text
使用 $apc-feature、$apc-miuix、$apc-data、$apc-ui、$apc-nav 和 $apc-preview。

目标：新增商品分页列表页面，并支持进入商品详情。
范围：仅修改目标 Feature、对应 Routes、Graph、Navigator 和相关测试。
约束：遵循 Route → Screen → Content；复用分页基类、设计系统和公共 UI；Preview 使用静态数据，不发起真实请求。
完成条件：页面能够显示 Loading、Success、Empty、Error；详情路由可传递商品 ID；assembleDebug 和 testDebugUnitTest 通过。
```

应用名称、包名、接口契约、路由参数或产品规则缺失时，应先补充这些输入，再允许 AI 写入工程。缺少关键事实时，AI 应继续查找源码、文档和测试，无法确认时明确说明，不应自行创建 API。

## 常见任务组合

| 开发目标 | 推荐 Skill | 主要阅读内容 |
| --- | --- | --- |
| 初始化应用项目 | `apc-setup`、`apc-build` | 环境要求、快速开始、构建配置和应用资源 |
| 新增普通业务页面 | `apc-feature`、`apc-miuix`、`apc-ui`、`apc-nav`、`apc-preview` | Feature 目录、Miuix 组件、View、ViewModel、页面模板和导航 |
| 新增网络分页页面 | `apc-feature`、`apc-data`、`apc-ui`、`apc-nav`、`apc-preview` | 数据层、结果处理、分页基类、页面状态和预览 |
| 修改主题或布局规范 | `apc-theme`、`apc-miuix`、`apc-ui` | Miuix 组件、设计系统、主题、UI 组件和屏幕适配 |
| 增加 Preview 数据 | `apc-preview`、`apc-data` | 注解、数据层预览数据和 View 预览规范 |
| 修改路由或结果回传 | `apc-nav` | 导航概览、路由、流程、拦截和结果回传 |
| 发布前检查工程质量 | `apc-audit`、`apc-build` | 架构、代码、测试、Gradle 和发布配置 |

## 使用边界

- 不根据其他平台、旧版本文档或类型名称猜测 Android、Compose、Hilt 或 Navigation 3 API。
- 不绕过 Repository、模块级 Navigator、设计系统、网络基类或页面分层另建一套实现。
- 不把业务专用代码放入 `core/`，不为了目录完整创建空目录或空抽象。
- 不在没有确认源码行为时修改 Gradle、版本目录、包名、签名或生成资源。
- 不覆盖工作区已有修改，不使用整体重置清理无关变更。
- 不以“代码已生成”作为完成标准；必须运行与风险匹配的构建、测试、静态检查或文档检查。
- 不把任务描述、协作过程或修改历史写入代码注释、文档正文和提交说明。

## 结果确认与交付

代码任务至少应根据影响范围运行：

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
git diff --check
```

涉及静态检查或发布配置时，再追加 `./gradlew :app:lint` 和对应的 Release 构建。AI 的交付说明应区分：

- 已修改的文件和职责范围；
- 依据哪些文档、源码和测试确认实现；
- 实际运行过的命令及结果；
- 尚未验证的真机行为、外部服务或网络链接。

构建通过只代表代码满足编译条件，不能替代页面行为、架构边界和文档内容的人工复核。

## 从哪里继续

- 第一次使用脚手架：阅读[快速开始](./quick-start.md)和[项目架构与职责](./architecture.md)。
- 了解工程边界：阅读[工程组织与模块边界](./modularization.md)和[Core 概览](../框架核心/index.md)。
- 新建页面：阅读[目录与命名规范](../业务功能/structure.md)、[创建页面流程](../业务功能/create-page.md)和[页面模板](../业务功能/templates.md)。
- 编写界面：阅读[设计系统](../框架核心/designsystem.md)、[主题系统](../框架核心/theme.md)、[UI 组件](../框架核心/ui.md)和[屏幕适配](../框架核心/screen-adaptation.md)。
- 接入数据：阅读[数据层](../框架核心/data.md)、[网络请求](../框架核心/network.md)、[请求结果处理](../框架核心/result.md)和[分页列表](../框架核心/pagination.md)。
- 接入导航：阅读[导航概览](../导航/index.md)、[路由配置](../导航/router.md)和[参数传递与结果回传](../导航/result.md)。

## 官方参考

- [Android Skills](https://developer.android.com/tools/agents/android-skills)：了解 Android 开发专用 Skill 的使用方式。
- [Android CLI](https://developer.android.com/tools/agents/android-cli)：了解 Android 官方命令行 Agent 工具及其使用方式。
- [Android Studio Gemini](https://developer.android.com/studio/gemini/overview)：了解 Android Studio 中的 AI 辅助开发能力。
- [GitHub Copilot 仓库自定义指令](https://docs.github.com/en/copilot/customizing-copilot/adding-repository-custom-instructions-for-github-copilot)：了解如何让支持该能力的工具读取仓库级开发约定。
