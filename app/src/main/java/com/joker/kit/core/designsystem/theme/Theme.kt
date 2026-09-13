package com.joker.kit.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeColorSpec
import top.yukonga.miuix.kmp.theme.ThemeController
import top.yukonga.miuix.kmp.theme.ThemePaletteStyle

/**
 * 应用主题入口。
 *
 * Miuix 是颜色、文字样式和组件交互视觉的唯一主题来源。
 *
 * @param darkTheme 是否使用深色模式，默认跟随系统设置。
 * @param dynamicColor 是否启用 Monet 动态配色，默认关闭。
 * @param colorSchemeMode 可选的 Miuix 官方配色模式，传入后优先于其他模式参数。
 * @param keyColor Monet 动态配色的种子颜色，未设置时使用系统壁纸颜色。
 * @param colorSpec Monet 动态配色使用的颜色规范版本。
 * @param paletteStyle Monet 动态配色使用的调色板风格。
 * @param isDark System 模式下的深色状态覆盖值，未设置时跟随系统。
 * @param content 需要应用主题的 Compose 内容。
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    colorSchemeMode: ColorSchemeMode? = null,
    keyColor: Color? = null,
    colorSpec: ThemeColorSpec = ThemeColorSpec.Spec2021,
    paletteStyle: ThemePaletteStyle = ThemePaletteStyle.TonalSpot,
    isDark: Boolean? = null,
    content: @Composable () -> Unit,
) {
    // 根据调用方选项选择 Miuix 官方配色模式。
    val mode = colorSchemeMode ?: when {
        dynamicColor && darkTheme -> ColorSchemeMode.MonetDark
        dynamicColor -> ColorSchemeMode.MonetLight
        darkTheme -> ColorSchemeMode.Dark
        else -> ColorSchemeMode.Light
    }
    // 主题控制器按完整配置记忆，确保模式、种子色和调色板切换即时生效。
    val controller = remember(mode, keyColor, colorSpec, paletteStyle, isDark) {
        ThemeController(
            colorSchemeMode = mode,
            keyColor = keyColor,
            colorSpec = colorSpec,
            paletteStyle = paletteStyle,
            isDark = isDark,
        )
    }

    MiuixTheme(controller = controller, content = content)
}
