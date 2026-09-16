# Miuix Compose

项目使用 [Miuix Compose](https://compose-miuix-ui.github.io/miuix/zh_CN/) 作为页面组件、主题和交互视觉的基础库。本页说明如何查阅 Miuix API，以及如何把官方组件接入 AndroidProject-Compose 的 Design System、`core/ui` 和 Feature 页面。

## 三层职责

| 层级 | 内容 | 查阅位置 |
| --- | --- | --- |
| Miuix 官方组件层 | `Text`、`Button`、`Card`、`TopAppBar`、`Scaffold`、Preference、Navigation、PullToRefresh 等 | [Miuix 组件文档](https://compose-miuix-ui.github.io/miuix/zh_CN/components/) |
| 项目设计系统层 | `AppColors`、`AppTextStyles`、`Size.kt`、`Shape.kt`、基础布局封装 | [设计系统](./designsystem.md)、[主题系统](./theme.md) |
| 项目页面语义层 | `AppScaffold`、`MiuixPageLazyColumn`、网络状态、空态、加载态和刷新容器 | [UI 组件](./ui.md) |

遇到组件参数、默认样式或交互行为时，先查阅 Miuix 官方文档和当前依赖源码；遇到页面结构、状态处理和项目视觉令牌时，查阅本地 Core 文档。不要根据其他平台组件名称猜测 Kotlin 参数。

## 依赖与导入

当前依赖由版本目录统一管理：

| 依赖 | 用途 |
| --- | --- |
| `miuix-ui` | 主题、基础组件和通用交互 |
| `miuix-preference` | 设置项、开关、下拉选择等 Preference 组件 |
| `miuix-blur` | 顶部栏、导航栏和背景的模糊与液态效果 |
| `miuix-squircle` | 平滑圆角和超椭圆渲染能力 |

基础组件从 `top.yukonga.miuix.kmp.basic` 导入，主题从 `top.yukonga.miuix.kmp.theme` 导入，Preference 和 Blur 能力分别从对应 Miuix 包导入。页面不复制 Miuix 组件实现，只组合官方 API 和项目级参数。

```kotlin
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
private fun ActionCard(onClick: () -> Unit) {
    Card {
        Text(
            text = "打开详情",
            color = AppColors.onSurface,
            style = AppTextStyles.body1,
        )
        Button(onClick = onClick) {
            Text(text = "继续")
        }
    }
}
```

## 常用组件的项目接入方式

| 需求 | 优先组件或封装 | 项目约定 |
| --- | --- | --- |
| 页面顶部栏 | `AppScaffold` → Miuix `TopAppBar` | 普通返回操作传入 `onBackClick`，大标题由 `useLargeTopBar` 控制 |
| 页面容器 | Miuix `Scaffold` → `AppScaffold` | 统一处理背景、Insets、Snackbar、BottomBar、FAB 和嵌套滚动 |
| 页面滚动 | `MiuixPageLazyColumn` | 合并 Scaffold 内边距和页面内边距，启用越界回弹与滚动触感 |
| 文本 | Miuix `Text` | 颜色使用 `AppColors`，文字样式使用 `AppTextStyles` |
| 操作按钮 | Miuix `Button`、`TextButton`、`IconButton` | 使用 Miuix 默认交互和主题角色 |
| 卡片和容器 | Miuix `Card` | 圆角和内边距优先使用 Miuix `Defaults` |
| 设置列表 | `ArrowPreference`、`SwitchPreference`、`OverlayDropdownPreference` | 设置状态由 ViewModel 或应用状态持有，Preference 只转发事件 |
| 刷新与加载更多 | `RefreshLayout`、`RefreshContent` | 状态来自分页 ViewModel，不在 Composable 内维护第二份状态 |
| 空态与错误 | `EmptyData`、`EmptyError`、`EmptyNetwork` | 由网络状态容器决定展示时机 |
| 导航栏 | Miuix `NavigationBar`、`NavigationRail`、`FloatingNavigationBar` | 由 Main Feature 组合导航目的地和选中状态 |

## 设置页面与 Preference

Preference 组件适合设置页的单行配置、开关和选择项。项目中的 `SettingScreen` 使用 Miuix Preference 直接组合页面，不在 `core/designsystem` 再包一层同义组件。

```kotlin
SwitchPreference(
    title = "启用平滑圆角",
    summary = "使用 Miuix Squircle 渲染容器边角",
    checked = settings.enableSquircle,
    onCheckedChange = onSquircleChanged,
)
```

Preference 的标题、摘要、间距和交互状态由 Miuix 负责；页面只提供状态和事件回调。需要自定义区块标题时使用 Miuix `SmallTitle` 或 Feature 私有组件。

## 主题读取

所有 Miuix 组件都运行在 `AppTheme` 提供的 `MiuixTheme` 组合环境中：

```kotlin
@Composable
private fun SurfaceTitle() {
    Text(
        text = "账户设置",
        color = MiuixTheme.colorScheme.onSurface,
        style = MiuixTheme.textStyles.title3,
    )
}
```

项目页面优先使用 `AppColors` 和 `AppTextStyles`，它们只是对当前 Miuix 主题的响应式代理。不要在 Feature 中写固定颜色、固定字号或本地主题对象。

## Blur 与 Squircle

Miuix Blur 用于顶部栏、底部导航和背景的液态模糊效果。`AppScaffold` 已封装顶部栏模糊开关、模糊进度、Shader 能力检测和应用设置读取；页面通常只传递 `blurEnabled` 或保持默认值。

Squircle 通过 `LocalSquircleEnabled` 控制全局开关。需要局部效果时使用 Miuix 提供的 `squircleSurface`、`squircleClip` 或 `squircleBorder`。运行时不支持 Shader 时，组件应能够回退到普通圆角或普通背景。

## 使用边界

- Miuix 官方组件处理通用交互、视觉状态和动画；项目只补充页面语义和业务数据连接。
- `core/designsystem` 不复制 Miuix Button、Card、Preference 或 Navigation 的实现。
- `core/ui` 封装跨 Feature 的页面骨架和状态容器；业务专属组合留在对应 Feature。
- Composable 通过参数接收状态和回调，不直接获取 ViewModel、Repository 或 DataSource。
- 新增 Miuix 组件封装前，先确认至少有两个 Feature 使用相同结构，并提供浅色、深色和必要的窗口预览。

## 相关文档

- [Miuix 官方主题系统](https://compose-miuix-ui.github.io/miuix/zh_CN/guide/theme.html)
- [Miuix 官方颜色规范](https://compose-miuix-ui.github.io/miuix/zh_CN/guide/colors.html)
- [Miuix 官方组件文档](https://compose-miuix-ui.github.io/miuix/zh_CN/components/)
- [设计系统](./designsystem.md)
- [主题系统](./theme.md)
- [UI 组件](./ui.md)
