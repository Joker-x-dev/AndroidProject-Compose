# 主题系统

## 介绍

项目主题完全采用 Miuix 官方主题系统。`core/designsystem/theme/Theme.kt` 的 `AppTheme` 负责创建 `ThemeController` 并调用 `MiuixTheme`，页面通过 `MiuixTheme.colorScheme` 读取颜色，通过 `MiuixTheme.textStyles` 读取文字样式。

颜色和文字样式不在项目中重新维护固定值。`Color.kt` 与 `TextStyles.kt` 只提供响应式访问入口，实际值始终来自当前 Miuix 主题。

Miuix 官方资料：

- [颜色系统](https://compose-miuix-ui.github.io/miuix/zh_CN/guide/colors.html)
- [主题系统](https://compose-miuix-ui.github.io/miuix/zh_CN/guide/theme.html)
- [组件文档](https://compose-miuix-ui.github.io/miuix/zh_CN/components/)

## 主题模式

Miuix 官方提供六种 `ColorSchemeMode`：

| 模式 | 作用 |
| --- | --- |
| `System` | 跟随系统浅色或深色模式 |
| `Light` | 固定使用浅色方案 |
| `Dark` | 固定使用深色方案 |
| `MonetSystem` | 使用动态颜色并跟随系统明暗 |
| `MonetLight` | 使用动态颜色并固定浅色 |
| `MonetDark` | 使用动态颜色并固定深色 |

`AppTheme` 保留 `darkTheme` 和 `dynamicColor` 作为现有调用入口，同时支持通过 `colorSchemeMode` 直接选择上述官方模式。新页面需要明确主题模式时，优先传入 `ColorSchemeMode`。

Monet 模式还支持以下官方配置：

- `keyColor`：动态配色的种子颜色；为空时使用系统壁纸颜色。
- `ThemeColorSpec`：`Spec2021` 或 `Spec2025`。
- `ThemePaletteStyle`：`TonalSpot`、`Neutral`、`Vibrant`、`Expressive`、`Rainbow`、`FruitSalad`、`Monochrome`、`Fidelity`、`Content`。
- `isDark`：`System` 或 `MonetSystem` 模式下的明暗状态覆盖值。

`Spec2025` 只适用于官方支持的调色板风格，其余组合由 Miuix 按官方规则降级处理。

## 颜色访问

`Color.kt` 中的 `AppColors` 与 Miuix 颜色角色一一对应：

```kotlin
Modifier.background(AppColors.surface)

Text(
    text = "标题",
    color = AppColors.onBackground,
)
```

常用颜色角色：

| 使用场景 | 颜色角色 |
| --- | --- |
| 主操作和强调 | `primary`、`onPrimary`、`primaryContainer`、`onPrimaryContainer` |
| 错误反馈 | `error`、`onError`、`errorContainer`、`onErrorContainer` |
| 页面背景 | `background`、`onBackground` |
| 表面和卡片 | `surface`、`onSurface`、`surfaceContainer` |
| 辅助文字 | `onBackgroundVariant`、`onSurfaceSecondary`、`onSurfaceContainerVariant` |
| 边框和分隔线 | `outline`、`dividerLine` |
| 禁用状态 | `disabledPrimary`、`disabledOnPrimary`、`disabledOnSurface` |
| 滑块状态 | `sliderKeyPoint`、`sliderKeyPointForeground`、`sliderBackground` |
| 窗口遮罩 | `windowDimming` |

完整角色、用途和浅色/深色 ARGB 数值以 Miuix 官方颜色文档为准，不在项目文档中复制一份可能失效的色值表。

## 文字样式访问

`TextStyles.kt` 中的 `TextStyles` 与 Miuix 官方 `TextStyles` 对应：

```kotlin
Text(
    text = "页面标题",
    style = TextStyles.title2,
)
```

可用样式包括 `main`、`paragraph`、`body1`、`body2`、`button`、`footnote1`、`footnote2`、`headline1`、`headline2`、`subtitle`、`title1`、`title2`、`title3` 和 `title4`。

这些属性在组合上下文中读取当前主题。不要使用 `remember` 保存单个颜色或文字样式，也不要把主题值写入普通顶层常量。

## Size 与 Shape 边界

Miuix 没有覆盖整个应用的全局 `Size`、`Spacing` 或 `Shape` 主题对象。尺寸和圆角主要由各个组件的 `Defaults` 提供，例如：

- `CardDefaults.CornerRadius`
- `SnackbarDefaults.CornerRadius`
- `SnackbarDefaults.InsideMargin`
- `SearchBarDefaults.InsideMargin`
- `TooltipDefaults.PlainTooltipCornerRadius`
- `FloatingActionButtonDefaults.MinHeight`

项目继续保留 `Size.kt`，用于页面级间距和布局节奏；继续保留 `Shape.kt`，用于业务自定义容器的语义圆角。Miuix 组件优先使用官方 `Defaults`，自定义容器再使用项目令牌。

`miuix-squircle` 提供平滑圆角的渲染方式，不规定全局圆角数值。`cornerRadius` 仍由具体设计决定，Android API 33 以下会回退到普通圆角渲染。

## 主题修改规则

1. 颜色和文字优先查阅 Miuix 官方中文文档。
2. 新增颜色角色前确认 Miuix 是否已有对应语义。
3. 页面优先读取 `AppColors`、`TextStyles` 或 `MiuixTheme`。
4. Miuix 组件优先使用官方默认参数。
5. `Size.kt` 和 `Shape.kt` 只维护项目页面布局和自定义容器规则。
6. 主题修改后验证浅色、深色、System 和 Monet 模式。
