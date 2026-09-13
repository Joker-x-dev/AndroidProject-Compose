# 设计系统

## 介绍

`core/designsystem` 是跨 Feature 复用的视觉和基础布局入口。颜色与文字样式来自 Miuix 官方主题，项目通过访问入口统一使用当前主题，不复制固定色板或字体表。

设计系统负责：

- Miuix 主题入口和主题模式配置。
- Miuix 颜色、文字样式的响应式访问。
- 项目页面级间距和自定义容器圆角。
- 基础 Box、Column、Row、列表、滚动和 Spacer 封装。
- 项目资源图标的统一调用方式。

业务状态、网络请求、导航和 Repository 不属于 `core/designsystem`。

## 模块内容

| 路径 | 作用 | 典型入口 |
| --- | --- | --- |
| `theme/Theme.kt` | `ThemeController` 和 `MiuixTheme` 主题入口 | `AppTheme` |
| `theme/Color.kt` | Miuix 官方颜色角色的响应式访问 | `AppColors.primary` |
| `theme/TextStyles.kt` | Miuix 官方文字样式的响应式访问 | `TextStyles.body1` |
| `theme/Shape.kt` | 项目自定义容器圆角 | `ShapeSmall`、`ShapeMedium` |
| `theme/Size.kt` | 页面间距、内边距、分割线和指示器尺寸 | `SpacePaddingMedium` |
| `theme/Icon.kt` | 项目资源图标封装 | `CommonIcon`、`ArrowRightIcon` |
| `component/Box.kt` | Box 对齐、尺寸、圆角、边框和内边距 | `CenterBox`、`RoundedBox` |
| `component/Column.kt` | Column 排列和内容内边距 | `AppColumn`、`CardContentList` |
| `component/Row.kt` | Row 排列和对齐 | `AppRow`、`SpaceBetweenRow` |
| `component/LazyList.kt` | Lazy 列表和列表内边距 | `AppLazyColumn`、`AppLazyRow` |
| `component/Scroll.kt` | 纵向和横向滚动容器 | `VerticalScroll` |
| `component/Spacer.kt` | 语义化间距组件 | `SpaceVerticalSmall` |

## 颜色与文字

颜色和文字样式必须在组合上下文中读取：

```kotlin
@Composable
private fun ProfileSummary() {
    AppColumn {
        Text(
            text = "个人资料",
            color = AppColors.onBackground,
            style = TextStyles.title3,
        )
        SpaceVerticalSmall()
        Text(
            text = "资料同步完成",
            color = AppColors.onSurfaceContainerVariant,
            style = TextStyles.body2,
        )
    }
}
```

`AppColors` 的属性对应 Miuix `Colors` 的官方角色，`TextStyles` 的属性对应 Miuix `TextStyles` 的官方样式。两者都是 getter，不保存静态快照，因此可以响应主题模式变化。

## 尺寸和形状

Miuix 组件的尺寸、内边距和圆角由各自的 `Defaults` 提供。项目 `Size.kt` 只表达页面布局节奏，例如页面边距、区块间距和列表间距；项目 `Shape.kt` 只表达自定义容器的语义圆角。

示例：

```kotlin
Card(
    cornerRadius = CardDefaults.CornerRadius,
)

Modifier.padding(SpacePaddingLarge)

Modifier.clip(RoundedCornerShape(ShapeMedium))
```

平滑圆角属于渲染方式，由 `miuix-squircle` 的 `squircleSurface`、`squircleClip` 或 `squircleBorder` 提供。它没有全局圆角尺寸表，具体半径由页面设计决定。

## 使用边界

- 只改变布局默认值的组件放在 `core/designsystem/component`。
- 具有页面语义的 AppBar、Scaffold、Loading、Empty 和网络状态组件放在 `core/ui`。
- 具体业务卡片、业务弹窗和业务列表项放在对应 Feature。
- Feature 页面不直接创建第二套颜色、文字、间距或圆角规范。
- 新增 Miuix 主题角色前先确认官方 `Colors` 或 `TextStyles` 是否已有对应定义。

## 官方资料

- [Miuix 中文颜色规范](https://compose-miuix-ui.github.io/miuix/zh_CN/guide/colors.html)
- [Miuix 中文主题系统](https://compose-miuix-ui.github.io/miuix/zh_CN/guide/theme.html)
- [Miuix 中文组件文档](https://compose-miuix-ui.github.io/miuix/zh_CN/components/)
- [Miuix 中文平滑圆角文档](https://compose-miuix-ui.github.io/miuix/zh_CN/guide/squircle.html)
