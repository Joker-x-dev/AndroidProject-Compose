# 快速开始

## 介绍

本页说明如何获取 AndroidProject-Compose、完成 Gradle 同步并运行默认示例。完成后，应用会从 `MainRoutes.Main` 进入主页，可继续查看 Core、导航、扩展和关于页面。

## 前置条件

- 已安装能够使用 Android Gradle Plugin 9.3.1 的 Android Studio。
- Gradle daemon 使用 JDK 21；项目的 Java source/target compatibility 为 17。
- 已安装项目要求的 Android SDK Platform 37。
- 已安装 Git，并能访问 Google Maven、Maven Central 和 JitPack。
- 准备 Android 7.0（API 24）或更高版本的真机或模拟器。

环境版本和检查方法参见[环境要求](./environment.md)。

## 一、获取代码

在终端中选择一个仓库地址执行克隆。GitHub 与 Gitee 内容相同，无需同时克隆。

```bash
# GitHub 仓库
git clone https://github.com/Joker-x-dev/AndroidProject-Compose.git
cd AndroidProject-Compose
```

GitHub 访问不稳定时，可改用 Gitee。

```bash
# Gitee 镜像
git clone https://gitee.com/Joker-x-dev/AndroidProject-Compose.git
cd AndroidProject-Compose
```

执行完成后，当前目录应包含 `settings.gradle.kts`、`gradlew`、`gradle/` 和 `app/`。

## 二、检查构建 JDK

在项目根目录执行 Gradle Wrapper，确认 Gradle 实际使用的 JVM。

```bash
./gradlew --version
```

输出中的 `Daemon JVM` 应使用 JDK 21；`Launcher JVM` 可能随终端环境不同。项目在 `gradle/gradle-daemon-jvm.properties` 中固定 daemon toolchain 为 JetBrains JDK 21；若版本不一致，先检查 Android Studio 的 Gradle JDK 与该配置是否冲突，再重新同步项目。

::: tip
Gradle daemon 使用 JDK 21 运行构建，不代表应用以 Java 21 为编译目标。`app/build.gradle.kts` 仍将 Java source/target compatibility 配置为 17。
:::

## 三、同步并构建

使用 Android Studio 打开仓库根目录，等待 Gradle Sync 完成。首次同步会从 `google()`、`mavenCentral()` 和 JitPack 下载依赖，需要保持网络可用。

同步完成后，在项目根目录构建 Debug APK。

```bash
# 编译 app 模块的 Debug APK
./gradlew :app:assembleDebug
```

构建成功时，终端末尾会出现以下结果，APK 位于 `app/build/outputs/apk/debug/`。

```text
BUILD SUCCESSFUL
```

项目启用了 ARM ABI 分包并同时生成通用 APK，因此输出目录可能包含多个 Debug APK。

## 四、运行示例

1. 在 Android Studio 工具栏选择 `app` 运行配置。
2. 连接 API 24 或更高版本的真机，或启动符合要求的模拟器。
3. 选择 Debug 构建并点击 **Run**。
4. 等待应用启动，确认主页底部显示核心、导航、扩展和关于入口。

Debug 构建会为 `applicationId` 添加 `.debug` 后缀，因此可与 Release 版本同时安装。

## 五、认识默认示例

主页底部的四个入口不是四个独立应用，而是同一个 `MainRoute` 内切换的示例区域。第一次运行时，可以按下面顺序查看：

| 入口 | 可以验证什么 | 后续文档 |
| --- | --- | --- |
| 核心 | 网络请求、分页、Room、本地存储、状态与屏幕适配 | [Core 核心能力](../框架核心/index.md) |
| 导航 | 普通跳转、带参跳转、登录拦截和结果回传 | [导航概览](../导航/index.md) |
| 扩展 | 当前提供屏幕适配示例，可观察窗口断点、列数、字号和尺寸变化 | [屏幕适配](../框架核心/screen-adaptation.md) |
| 关于 | 项目地址、文档与交流入口 | [社区与反馈](./community.md) |

建议先打开“核心”中的 Network Demo 和 Network List Demo。前者展示单对象请求的 Loading、Success、Error，后者展示分页列表额外包含的 Empty、刷新和加载更多状态。看到页面行为后，再阅读对应基类文档会更容易理解。

::: tip 从示例开始改业务
不要直接在 Demo 页面里接入正式业务。先复制页面结构，在 `feature/<domain>/` 创建自己的 Route、Screen 和 ViewModel，再通过 Repository 连接数据源。完整流程参见[创建页面流程](../业务功能/create-page.md)。
:::

## 验证结果

完成以下检查即可确认基础环境可用：

- `./gradlew --version` 显示 Gradle daemon 使用 JDK 21。
- `./gradlew :app:assembleDebug` 返回 `BUILD SUCCESSFUL`。
- Android Studio 可以安装并启动 `app`。
- 默认页面为 `MainRoutes.Main` 对应的主页，而不是登录页。
- “核心”入口可以打开网络、分页、数据库和本地存储等 Demo 页面。

如需补充最小测试验证，可执行：

```bash
# 运行 app 模块的 Debug 单元测试
./gradlew :app:testDebugUnitTest
```

## 常见问题

### Gradle daemon 使用的 JVM 不是 21

项目通过 `gradle/gradle-daemon-jvm.properties` 请求 JetBrains JDK 21。若 `./gradlew --version` 显示的 daemon JVM 不符合要求，检查 Android Studio 的 Gradle JDK、终端 JDK 和 Gradle daemon toolchain 配置，不要把 `compileOptions` 中的 Java 17 兼容级别当作 daemon JDK。

### 提示找不到 Android SDK Platform 37

打开 Android Studio 的 SDK Manager，安装项目所需的 Android SDK Platform 37，然后重新执行 Gradle Sync。不要把 `compileSdk` 降级来绕过缺失的本地 SDK。

### 依赖下载失败

先确认能够访问 Google Maven、Maven Central 和 JitPack，再重试同步。Gradle daemon JDK 21 尚未安装时，还需要访问 `api.foojay.io` 解析并下载项目声明的 JetBrains JDK toolchain。项目在 `settings.gradle.kts` 中使用 `FAIL_ON_PROJECT_REPOS`，依赖仓库应统一维护在该文件中。

## 相关链接

- [Android Studio 安装指南](https://developer.android.com/studio/install)
- [Android 构建中的 JDK 版本](https://developer.android.com/build/jdks)
- [使用 Android Studio 运行应用](https://developer.android.com/studio/run)
- [Gradle Wrapper 基础用法](https://docs.gradle.org/current/userguide/gradle_wrapper.html)
