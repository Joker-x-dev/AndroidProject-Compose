# 环境要求

## 介绍

本页列出当前源码实际使用的开发、构建和运行版本。版本值来自 `gradle/libs.versions.toml`、`gradle/wrapper/gradle-wrapper.properties`、`gradle/gradle-daemon-jvm.properties` 与 `app/build.gradle.kts`，用于排查 Gradle Sync、编译和设备安装问题。

## 一、版本基线

| 项目 | 当前版本或要求 | 配置来源 |
| --- | --- | --- |
| Gradle daemon JDK | JetBrains JDK 21 | `gradle/gradle-daemon-jvm.properties` 的 toolchain 配置。 |
| Java source/target compatibility | 17 | `app/build.gradle.kts` 的 `compileOptions`。 |
| Gradle Wrapper | 9.7.0 | `gradle/wrapper/gradle-wrapper.properties`。 |
| Android Gradle Plugin | 9.3.1 | `gradle/libs.versions.toml`。 |
| Kotlin | 2.4.10 | `gradle/libs.versions.toml`。 |
| Compose BOM | 2026.08.00 | `gradle/libs.versions.toml`。 |
| Navigation 3 | 1.1.6 | `gradle/libs.versions.toml`。 |
| `compileSdk` | 37 | `app/build.gradle.kts`。 |
| `targetSdk` | 37 | `app/build.gradle.kts`。 |
| `minSdk` | 24 | `app/build.gradle.kts`，最低支持 Android 7.0；Miuix Preference 要求 API 24+。 |

项目没有锁定具体 Android Studio 版本。所选 IDE 必须支持 Android Gradle Plugin 9.3.1，并能安装 Android SDK Platform 37。

## 二、配置构建 JDK

Gradle daemon JDK 与应用的 Java 编译兼容级别是两项独立配置：

- `gradle/gradle-daemon-jvm.properties` 请求 JetBrains JDK 21，用于运行 Gradle daemon。
- `app/build.gradle.kts` 将 `sourceCompatibility` 与 `targetCompatibility` 设为 Java 17，用于约束 Java 源码和生成字节码的兼容级别。

Android Studio 内的 Gradle JDK 与终端启动 JVM 可能不同，但最终 daemon 必须满足项目声明的 JDK 21 toolchain。

在项目根目录执行以下命令，检查 Gradle Wrapper 实际使用的版本。

```bash
./gradlew --version
```

确认输出中的 `Launcher JVM` 与 `Daemon JVM`，重点检查 daemon 使用 JDK 21。若仅 IDE 同步失败，再检查 Android Studio 的 Gradle JDK 设置。

::: warning
不要把 Java 17 source/target compatibility 写成“项目运行 Gradle 所需的 JDK 17”。前者约束应用代码兼容性，后者由 Gradle daemon 的 JDK 21 toolchain 决定。
:::

## 三、安装 Android SDK

在 Android Studio 的 SDK Manager 中安装以下组件：

- Android SDK Platform 37。
- Android SDK Platform-Tools。
- Android SDK Command-line Tools，用于命令行管理 SDK 时安装。
- Android Emulator 和所需系统镜像，仅在使用模拟器时安装。

Build Tools 版本未在项目中固定，由 Android Gradle Plugin 根据构建配置选择。无需在文档外另行指定一个固定版本。

## 四、准备运行设备

- **真机**：Android 7.0（API 24）或更高版本，并开启开发者选项与 USB 调试。
- **模拟器**：创建 API 24 或更高版本的虚拟设备；验证 `targetSdk` 相关行为时，优先使用 API 37 系统镜像。
- **ABI**：项目配置了 `armeabi-v7a`、`arm64-v8a` 分包，并额外生成通用 APK。

## 五、检查依赖网络

`settings.gradle.kts` 统一声明以下依赖仓库：

- Google Maven，用于 AndroidX、Compose 和 Android Gradle Plugin。
- Maven Central，用于 Kotlin 与多数第三方依赖。
- JitPack，用于由 Git 仓库发布的依赖。
- Foojay Disco API，用于 Gradle daemon toolchain 自动解析 JetBrains JDK 21；对应地址记录在 `gradle/gradle-daemon-jvm.properties`。

公司网络或代理环境必须允许访问这些仓库。项目启用了 `RepositoriesMode.FAIL_ON_PROJECT_REPOS`，新增仓库时应修改 `settings.gradle.kts`，不要在单个模块内重复声明。

## 验证环境

依次执行以下命令，先确认工具链，再验证完整构建。

```bash
# 检查 Gradle 与 JVM
./gradlew --version

# 验证 SDK、插件和依赖是否可用
./gradlew :app:assembleDebug
```

两个命令均成功，且第二个命令返回 `BUILD SUCCESSFUL`，说明本地环境满足项目的基础构建要求。设备安装和运行仍需按照[快速开始](./quick-start.md)单独验证。

## 常见问题

### Android Studio 无法识别 AGP 9.3.1

先升级到支持该插件版本的 Android Studio，再重新同步。不要只修改 AGP 版本，因为插件版本同时受 Gradle、Kotlin 与 Android Studio 支持范围约束。

### 命令行构建成功但 IDE 同步失败

优先比较命令行 JVM 与 Android Studio Gradle JDK 是否一致，再检查 IDE 使用的 Android SDK 路径和已安装平台。

### API 24 设备无法验证 API 37 行为

`minSdk = 24` 只表示应用可以安装的最低版本。与 `targetSdk = 37` 相关的行为变更需要在 API 37 设备或模拟器上验证。

## 官方文档

- [Android Studio 安装指南](https://developer.android.com/studio/install)
- [Android 构建中的 JDK 版本](https://developer.android.com/build/jdks)
- [使用 SDK Manager 更新工具](https://developer.android.com/studio/intro/update#sdk-manager)
- [Android SDK 平台发布说明](https://developer.android.com/tools/releases/platforms)
- [Android Gradle Plugin 发布说明](https://developer.android.com/build/releases/gradle-plugin)

环境验证完成后，继续阅读[工程组织与模块边界](./modularization.md)，先理解 `core/` 与 `feature/` 的职责，再进入具体能力章节。
