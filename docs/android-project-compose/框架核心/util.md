# 工具类

## 介绍

`core/util` 存放与页面无关的轻量工具：权限、底层 MMKV 封装、应用包信息和时间计算。工具类不承载页面状态；需要在启动阶段准备的能力由 `Application.kt` 初始化。全局提示与确认框由 `core/state` 和 `core/ui/component` 承载。

工具类适合无业务归属、输入输出明确的重复操作。需要 Repository、页面生命周期或多步业务状态的逻辑不应塞进 `core/util`。

## 工具清单

| 文件 | 主要入口 | 依赖/边界 |
| --- | --- | --- |
| `permission/PermissionUtils.kt` | `request*Permission`、`hasPermission`、`openPermissionSettings` | XXPermissions；需要 Activity 或可解析到 Activity 的 Context |
| `storage/MMKVUtils.kt` | `init`、`getInstance`、`put*`、`get*` | Tencent MMKV；使用前必须初始化 |
| `package/PackageUtils.kt` | `getCurrentAppName`、版本信息、签名和安装状态 | Android PackageManager |
| `time/TimeUtils.kt` | `calculateRemainingDuration` | 纯时间计算，无 Android UI 依赖 |

## 初始化边界

`Application.onCreate()` 当前按顺序初始化全局 Snackbar 上下文、Timber、MMKV 和依赖应用设置的通用能力，然后初始化依赖 MMKV 的 `UserState`。Miuix `PullToRefresh` 在 `RefreshLayout` 内按页面状态使用，不需要单独初始化。工具页只说明入口归属，不重复各能力的初始化教程：

| 能力 | 初始化入口 | 详细说明 |
| --- | --- | --- |
| 全局提示 | `AppSnackbar.initialize(this)` | [全局状态](./state.md) 与 MainActivity 中的 `AppSnackbarHost` |
| Timber | `Application.initLog()` | Debug 构建种植 `DebugTree` |
| MMKV | `Application.initMMKV()` | [本地存储](./datastore.md#初始化顺序) |
| 刷新组件 | `RefreshLayout` / Miuix `PullToRefresh` | [分页列表](./pagination.md#refreshlayout-行为) |
| 用户状态 | `userState.initialize()` | [全局状态](./state.md#userstate-初始化与写入) |

不要另建第二个 `Application` 类。新增必须全局初始化的工具时，接入现有入口并确认初始化顺序；领域存储、页面刷新和全局状态的完整行为由对应章节负责。

## 存储工具边界

`MMKVUtils` 虽然位于 `core/util/storage`，但它只是当前本地存储能力的底层实现。Feature 应优先调用 Store Repository，不应直接创建 key 或处理 JSON。初始化、领域 DataSource、Repository、常用 API 和失败恢复集中在[本地存储](./datastore.md)说明。

## 权限请求

权限工具提供存储、相机、相册、通知、录音、位置、相机与相册组合权限，以及 `requestCustomPermissions`。回调返回是否全部授予；被永久拒绝时工具会提示并可打开系统权限设置。权限申请仍需在 Manifest 声明权限，并根据 Android 版本选择实际权限。

以下片段展示通知权限回调的最小调用方式。

```kotlin
import android.content.Context
import com.joker.kit.core.util.permission.PermissionUtils

/**
 * 请求通知权限并根据结果继续业务流程
 *
 * @param context Activity 或可解析到 Activity 的上下文
 */
fun requestNotification(context: Context) {
    PermissionUtils.requestNotificationPermission(context) { granted ->
        // 只有授权成功才执行需要通知权限的操作。
        if (granted) {
            startNotificationWork()
        }
    }
}

/**
 * 启动依赖通知权限的后台任务
 */
private fun startNotificationWork() = Unit
```

传入的 `Context` 必须是 Activity，或能够通过 `ContextWrapper` 解析到 Activity。解析失败时工具会显示错误提示、回调 `false` 并停止申请。永久拒绝时工具会打开权限设置页；普通拒绝只提示失败。

## 包信息与时间工具

以下片段组合版本读取和最短展示时长计算，适合启动页等需要等待动画的场景。

```kotlin
import android.content.Context
import com.joker.kit.core.util.`package`.PackageUtils
import com.joker.kit.core.util.time.TimeUtils

/**
 * 计算启动页剩余展示时间并读取版本名称
 *
 * @param context 应用上下文
 * @param startTime 启动页开始时间戳
 * @return 版本名称与剩余展示毫秒数
 */
fun startupInfo(context: Context, startTime: Long): Pair<String, Long> {
    // 当前应用版本名称
    val versionName = PackageUtils.getCurrentVersionName(context)
    // 启动页达到最短展示时长前的剩余毫秒数
    val remaining = TimeUtils.calculateRemainingDuration(
        startTime = startTime,
        minDurationMillis = 320L,
    )
    return versionName to remaining
}
```

`PackageUtils` 获取失败时返回空字符串、`0L` 或 `null`，调用方应把失败当作可处理分支；`TimeUtils` 会将已达到最短时长的结果限制为 `0L`。

## 选择与失败处理

| 需求 | 使用入口 | 失败时 |
| --- | --- | --- |
| 显示短暂操作反馈 | `AppSnackbar.show*` | 交由 MainActivity 全局宿主展示，无宿主时消息丢弃 |
| 请求运行时权限 | `PermissionUtils.request*` | 回调 `false`，不要继续受保护操作 |
| 保存小型键值或序列化对象 | 对应 Store Repository；底层才使用 `MMKVUtils` | 未初始化会抛异常，解析失败返回空值 |
| 读取版本与签名 | `PackageUtils` | 处理空字符串、`0L` 或 `null` |
| 保证最短动画时长 | `TimeUtils.calculateRemainingDuration` | 返回值最低为 `0L` |

权限示例还必须在 `AndroidManifest.xml` 声明相应权限。Android 不同版本的媒体、通知和位置权限存在差异，应以 XXPermissions 当前权限对象和 Android 官方说明为准。

## 新增工具类

1. 先确认逻辑不依赖具体 Feature、Repository 或 Compose 状态。
2. 在 `core/util/<domain>` 下按职责建包，公开方法补齐 KDoc、输入、返回值和失败行为。
3. 需要第三方库时在版本目录和 `app/build.gradle.kts` 中统一声明。
4. 需要初始化时接入现有 `Application.kt`，并提供未初始化的明确错误。
5. 为纯函数编写单元测试；依赖 Context 的工具至少验证成功和失败分支。

## 相关链接

- [XXPermissions](https://github.com/getActivity/XXPermissions)
- [MMKV](https://github.com/Tencent/MMKV)
- [Android 权限请求](https://developer.android.com/training/permissions/requesting)
- [Android PackageManager](https://developer.android.com/reference/android/content/pm/PackageManager)
