# 设计系统

## 定位

`core/designsystem` 是项目级视觉令牌和基础布局封装，以 Miuix Compose 为组件和主题基础。

设计系统负责：

- 通过 `MiuixTheme` 提供主题颜色和文字样式的响应式访问。
- 暴露页面间距、内边距、分割线和指示器尺寸。
- 暴露项目自定义圆角和基础容器布局。
- 提供 Box、Column、Row、Lazy 列表、滚动和 Spacer 封装。
- 提供项目资源图标入口。

页面状态、网络请求、导航和 Repository 不属于设计系统。

## 目录与入口

| 路径 | 职责 | 常用入口 |
| --- | --- | --- |
| `theme/Theme.kt` | 创建 `ThemeController`，接入 Miuix 主题 | `AppTheme`、`isInDarkTheme` |
| `theme/Color.kt` | Miuix `ColorScheme` 的响应式访问 | `AppColors.primary` |
| `theme/TextStyles.kt` | Miuix `TextStyles` 的响应式访问 | `AppTextStyles.body1` |
| `theme/Size.kt` | 页面间距、内边距、分割线、指示器 | `SpacePaddingLarge` |
| `theme/Shape.kt` | 项目自定义圆角和 `Shape` | `ShapeMedium`、`RadiusLarge` |
| `theme/Icon.kt` | 项目资源图标封装 | `CommonIcon`、`ArrowRightIcon` |
| `component/Box.kt` | 对齐、填充、尺寸、边框和内边距 | `AppBox`、`CenterBox` |
| `component/Column.kt` | 垂直排列和内容内边距 | `AppColumn`、`CenterColumn` |
| `component/Row.kt` | 水平排列和对齐 | `AppRow`、`SpaceBetweenRow` |
| `component/LazyList.kt` | LazyColumn、LazyRow 的常用封装 | `AppLazyColumn`、`AppLazyRow` |
| `component/Scroll.kt` | 纵向和横向滚动修饰符 | `VerticalScroll` |
| `component/Spacer.kt` | 语义化固定间距 | `SpaceVerticalSmall()` |

## 主题令牌的读取方式

`AppColors` 和 `AppTextStyles` 的属性都是 `@Composable @ReadOnlyComposable` getter，实际值来自当前 `MiuixTheme`。必须在组合上下文中读取，不能保存为普通顶层常量或通过 `remember` 缓存单个颜色、文字样式。

```kotlin
@Composable
private fun ProfileSummary() {
    AppColumn {
        Text(
            text = "个人资料",
            color = AppColors.onBackground,
            style = AppTextStyles.title3,
        )
        SpaceVerticalSmall()
        Text(
            text = "资料同步完成",
            color = AppColors.onSurfaceSecondary,
            style = AppTextStyles.body2,
        )
    }
}
```

页面也可以直接读取 `MiuixTheme.colorScheme` 和 `MiuixTheme.textStyles`；使用 `AppColors`、`AppTextStyles` 可以让项目代码保持统一入口。

## 尺寸、圆角与平滑圆角

`Size.kt` 是项目页面布局节奏，不是 Miuix 组件的全局尺寸主题。当前尺寸令牌包括：

| 类别 | 令牌 |
| --- | --- |
| 垂直间距 | `SpaceVerticalXSmall`、`Small`、`Medium`、`Large`、`XLarge`、`XXLarge` |
| 水平间距 | `SpaceHorizontalXSmall`、`Small`、`Medium`、`Large`、`XLarge`、`XXLarge` |
| 内边距 | `SpacePaddingXSmall`、`Small`、`Medium`、`Large` |
| 其他尺寸 | `SpaceDivider`、`SpaceIndicator` |

相邻内容的固定留白优先使用 `SpaceVertical*()` 或 `SpaceHorizontal*()`；动态尺寸和 Miuix 组件内部尺寸使用组件参数或官方 `Defaults`。

`Shape.kt` 提供 `RadiusXSmall` 到 `RadiusExtraLarge`、对应的 `Shape*` 以及 `ShapeCircle`。Miuix 的 `Card`、Snackbar 等组件应优先使用自身 `Defaults`；项目自定义容器才使用这些令牌。

平滑圆角是 Miuix Squircle 的渲染能力，不提供全局半径表。需要液态或超椭圆效果时使用 Miuix 的 `squircleSurface`、`squircleClip` 或 `squircleBorder`，并遵循 `LocalSquircleEnabled` 的应用设置。

## 使用边界

- Miuix 官方组件负责 Button、Text、Card、TopAppBar、Scaffold、Preference、Navigation 和刷新等交互语义。
- `core/designsystem/component` 只放跨页面复用的布局便利封装，不复制 Miuix 组件行为。
- 页面级 AppScaffold、空态、加载态、网络状态、刷新容器和自适应能力放在 `core/ui`。
- 业务卡片、业务弹窗和业务列表项留在对应 Feature。
- Feature 不直接创建第二套颜色、文字、间距或圆角规范。

## 参考资料

- [Miuix 中文颜色规范](https://compose-miuix-ui.github.io/miuix/zh_CN/guide/colors.html)
- [Miuix 中文主题系统](https://compose-miuix-ui.github.io/miuix/zh_CN/guide/theme.html)
- [Miuix 中文组件文档](https://compose-miuix-ui.github.io/miuix/zh_CN/components/)
- [Miuix 中文平滑圆角文档](https://compose-miuix-ui.github.io/miuix/zh_CN/guide/squircle.html)
