---
title: "AndroidProject-Compose"
pageClass: "android-project-about"
---

<p align="center">
  <img alt="AndroidProject-Compose Logo" src="../../images/graphs/logo.svg" width="100">
</p>

# AndroidProject-Compose

<p align="center">一个基于 Kotlin、Jetpack Compose 和 Navigation 3 的 Android 快速开发框架</p>

<p align="center">
  <a href="https://github.com/Joker-x-dev/AndroidProject-Compose" target="_blank" rel="noopener noreferrer">GitHub</a>
  &nbsp;
  ·
  &nbsp;
  <a href="https://gitee.com/Joker-x-dev/AndroidProject-Compose" target="_blank" rel="noopener noreferrer">Gitee</a>
</p>

## 介绍

AndroidProject-Compose 是一个基于 Kotlin、Jetpack Compose 和 Navigation 3 的 Android 快速开发框架。项目提供网络、数据库、本地存储、状态管理、设计系统和导航等基础能力，并通过可运行的 Feature 页面展示这些能力的接入方式。

框架适合用作安卓应用的工程起点或 Compose 架构示例。它提供的是基础设施与目录约定，不包含完整业务系统；需要完整业务流程时，可参考由同一套实践演进而来的[青商城](https://github.com/Joker-x-dev/CoolMallKotlin)项目。

## 适用场景

- 使用 Kotlin 与 Jetpack Compose 创建新应用，希望复用现成的基础设施。
- 需要统一页面、ViewModel、数据访问和导航的组织方式。
- 希望在一个 Gradle 模块中快速验证业务，再根据团队规模决定是否拆分模块。
- 需要查看网络、分页、Room、本地存储、状态管理或 Navigation 3 的可运行示例。

## 设计目标

- **保持启动路径简短**：仓库只包含 `:app` 模块，克隆后可直接同步和运行。
- **明确代码职责**：源码按 `core/` 与 `feature/` 组织，页面统一使用 Route → Screen → Content 三层结构。
- **统一数据入口**：Feature 中的 ViewModel 通过 Repository 访问网络、数据库或本地存储数据源。
- **集中管理导航**：`core/navigation` 提供导航运行时与路由定义，`feature/*/navigation` 注册业务页面。
- **提供真实示例**：默认包含认证、主页、用户信息和基础能力演示页面。

## 核心术语

后续章节会反复使用下面这些名称。初次阅读只需要理解它们各自负责什么，不需要立刻记住所有源码位置。

| 术语 | 在项目中的含义 |
| --- | --- |
| Route | 页面与框架能力的连接层。它获取 ViewModel、收集状态，再把状态和事件传给 Screen。 |
| Screen | 页面骨架层。它接收状态和回调，负责 `Scaffold`、通用容器与缺省状态，不直接请求数据。 |
| Content | 最终内容层。它只绘制普通页面或成功状态下的业务布局，通过回调上报用户操作。 |
| Graph | Navigation 3 的页面注册表，负责把一个 `NavKey` 路由对象映射到对应 Route。 |
| Repository | ViewModel 访问网络、Room 和本地存储的统一入口，负责选择或组合数据源。 |
| NavKey | Navigation 3 回退栈中的类型安全路由对象，用于标识目标页面及其参数。 |
| Navigator | 业务域的导航入口，把“打开某页面”等业务动作转换为具体 `NavKey` 和导航操作。 |

## 工程组成

| 目录 | 职责 |
| --- | --- |
| `core/` | 基类、数据层、导航、状态、设计系统、通用 UI 与工具能力。 |
| `feature/` | `auth`、`demo`、`main`、`user` 等业务页面、ViewModel 与 Graph。 |
| `MainActivity.kt` | 创建 Compose 内容并挂载 `AppNavHost`。 |
| `Application.kt` | 初始化 Hilt、Timber、MMKV、Toast 和用户状态；Miuix 刷新组件由页面 `RefreshLayout` 管理。 |
| `MainActivityViewModel.kt` | 预留的空 ViewModel 类，`MainActivity` 当前未获取或引用它。 |
| `gradle/libs.versions.toml` | 集中维护插件与依赖版本。 |
| `app/build.gradle.kts` | 配置 SDK、构建类型、ABI、Java 版本和应用依赖。 |

当前工程只有一个 Gradle 应用模块。`core` 和 `feature` 是 `:app` 内的逻辑分层，不是可独立编译的 Gradle 子模块。详细边界参见[工程组织与模块边界](./modularization.md)。

文档中的文件位置统一使用 `core/`、`feature/<domain>/` 等逻辑目录，不把源码包名或本地文件系统路径作为固定约定。可复制 Kotlin 示例中的 `package` 和 `import` 使用当前代码基线的包名；迁移到其他包名时，使用 Android Studio 的重构与自动导入同步更新。

## 核心能力

| 能力 | 当前实现 |
| --- | --- |
| UI | Jetpack Compose、Miuix Compose、Material 3 Adaptive 窗口信息、统一主题与通用组件。 |
| 导航 | Navigation 3、类型安全 `NavKey`、模块 Graph、登录拦截、结果回传与页面状态保存。 |
| 状态 | `StateFlow`、Feature ViewModel、应用级 `UserState` 和示例计数状态。 |
| 网络 | Retrofit、OkHttp、Kotlinx Serialization、统一网络响应与数据源。 |
| 数据 | Repository 聚合网络、Room 数据库和本地存储；当前本地存储实现为 MMKV。 |
| 工程能力 | Hilt、KSP、Timber、Chucker、LeakCanary、Compose Preview。 |

## 阅读路径

1. 按照[快速开始](./quick-start.md)完成同步、构建和运行。
2. 在[环境要求](./environment.md)中核对 JDK、SDK 与构建工具版本。
3. 阅读[工程组织与模块边界](./modularization.md)，理解单模块内的目录职责。
4. 阅读[项目架构与职责](./architecture.md)，了解启动、页面、导航和数据访问链路。
5. 从 [Core 核心能力](../框架核心/index.md)开始学习主题、状态、网络、存储和分页等前置能力。
6. Core 阅读完成后，再进入[导航概览](../导航/index.md)和[Feature 模块概览](../业务功能/index.md)。

::: tip 初次阅读建议
不要直接从页面模板开始复制代码。Feature 中的页面会依赖 Core 的主题、网络状态、Repository 和导航能力；按上面的顺序阅读，才能知道模板中的每一层为什么存在。
:::

## 项目地址

- [AndroidProject-Compose GitHub 仓库](https://github.com/Joker-x-dev/AndroidProject-Compose)
- [AndroidProject-Compose Gitee 仓库](https://gitee.com/Joker-x-dev/AndroidProject-Compose)
- [青商城 Android 示例项目](https://github.com/Joker-x-dev/CoolMallKotlin)
- [Jetpack Compose 官方概览](https://developer.android.com/develop/ui/compose)
