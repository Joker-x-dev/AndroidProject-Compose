# 主题系统

## 主题链路

项目主题完全采用 Miuix 官方主题系统。应用入口 `MainActivity` 根据 `AppSettingsState` 选择模式和 Monet 参数，`AppTheme` 创建 `ThemeController` 并调用 `MiuixTheme`，页面从 `MiuixTheme` 或项目代理入口读取当前颜色和文字样式。

```text
AppSettingsState.uiState
  → ColorSchemeMode、keyColor、ThemeColorSpec、ThemePaletteStyle
  → AppTheme
  → ThemeController
  → MiuixTheme
  → AppColors / AppTextStyles / MiuixTheme
```

Miuix 是颜色、文字样式和组件视觉的唯一主题来源。

## 配色模式

Miuix `ColorSchemeMode` 当前支持六种模式：

| 模式 | 行为 |
| --- | --- |
| `System` | 跟随系统明暗，不启用动态配色 |
| `Light` | 固定浅色方案 |
| `Dark` | 固定深色方案 |
| `MonetSystem` | 使用系统动态颜色并跟随系统明暗 |
| `MonetLight` | 使用动态颜色并固定浅色 |
| `MonetDark` | 使用动态颜色并固定深色 |

`AppTheme` 通过 `colorSchemeMode` 选择配色模式；未显式传入时，根据 `darkTheme` 和 `dynamicColor` 计算 Light、Dark 或 Monet 模式。

Monet 还支持：

- `keyColor`：动态配色种子色，为 `null` 时使用系统壁纸颜色。
- `ThemeColorSpec`：`Spec2021` 或 `Spec2025`。
- `ThemePaletteStyle`：`TonalSpot`、`Neutral`、`Vibrant`、`Expressive`、`Rainbow`、`FruitSalad`、`Monochrome`、`Fidelity`、`Content`。
- `isDark`：覆盖 `System` 或 `MonetSystem` 的明暗判断。

应用设置中的主题模式、种子色、颜色规范和调色板索引由 `AppSettingsState` 保存；`MainActivity` 将索引安全映射为 Miuix 枚举，非法值回退到默认项。

## 颜色访问

`Color.kt` 中的 `AppColors` 是 `MiuixTheme.colorScheme` 的项目代理。它覆盖主色、次要色、三级色、错误色、容器色、背景、表面、边框、分隔线、禁用态、滑块和窗口遮罩等角色。

```kotlin
@Composable
private fun StatusLabel(isError: Boolean) {
    Text(
        text = if (isError) "请求失败" else "同步完成",
        color = if (isError) AppColors.error else AppColors.primary,
        style = AppTextStyles.body2,
    )
}
```

常用选择：

| 场景 | 角色 |
| --- | --- |
| 页面背景 | `background`、`onBackground`、`onBackgroundVariant` |
| 卡片和表面 | `surface`、`onSurface`、`surfaceContainer` |
| 强调和操作 | `primary`、`onPrimary`、`primaryContainer` |
| 次级操作 | `secondary`、`secondaryContainer` |
| 错误反馈 | `error`、`onError`、`errorContainer` |
| 边框与分隔 | `outline`、`dividerLine` |
| 禁用状态 | `disabledPrimary`、`disabledOnPrimary`、`disabledOnSurface` |
| 弹窗遮罩和滑块 | `windowDimming`、`sliderBackground`、`sliderKeyPoint` |

完整色值由 Miuix 当前主题计算，不在项目文档中复制固定浅色或深色色板。

## 文字样式访问

`TextStyles.kt` 中的 `AppTextStyles` 代理 Miuix 官方文字样式：`main`、`paragraph`、`body1`、`body2`、`button`、`footnote1`、`footnote2`、`headline1`、`headline2`、`subtitle`、`title1`、`title2`、`title3` 和 `title4`。

```kotlin
Text(
    text = "页面标题",
    style = AppTextStyles.title2,
    color = AppColors.onBackground,
)
```

这些属性必须在组合上下文中读取，以便主题切换时随 `MiuixTheme` 重组。不要缓存单个 `TextStyle`，也不要在页面中重新声明一套字号和字重。

## 尺寸、Shape 与 Squircle

Miuix 组件的内部尺寸和圆角优先使用各组件的 `Defaults`；项目 `Size.kt` 只维护页面间距、内边距、分割线和指示器，`Shape.kt` 只维护自定义容器的 `Radius*` 和 `Shape*`。

Miuix Squircle 是可选渲染能力。应用通过 `LocalSquircleEnabled` 控制是否启用；API 受限或运行时 Shader 不可用时，相关组件回退到普通圆角或普通背景。

## 主题修改规则

1. 先查阅 Miuix 官方颜色、主题和组件文档，再确认源码中的实际 API。
2. 新增颜色或文字语义前，确认 Miuix 是否已有对应角色。
3. 页面优先使用 `AppColors`、`AppTextStyles` 或 `MiuixTheme`，禁止写固定色值和局部文字规范。
4. Miuix 组件优先使用官方默认参数；项目令牌只补充页面级布局规则。
5. 修改主题后验证 System、Light、Dark、MonetSystem、MonetLight 和 MonetDark，以及 Squircle 开关。

## 参考资料

- [Miuix 中文颜色规范](https://compose-miuix-ui.github.io/miuix/zh_CN/guide/colors.html)
- [Miuix 中文主题系统](https://compose-miuix-ui.github.io/miuix/zh_CN/guide/theme.html)
- [Miuix 中文组件文档](https://compose-miuix-ui.github.io/miuix/zh_CN/components/)
