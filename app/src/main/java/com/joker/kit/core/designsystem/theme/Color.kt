package com.joker.kit.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 颜色令牌访问入口
 */
object AppColors {
    /**
     * 主色，用于应用中的主要操作和强调内容。
     */
    val primary: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.primary

    /**
     * 主色上的文本。
     */
    val onPrimary: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onPrimary

    /**
     * 主色变体。
     */
    val primaryVariant: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.primaryVariant

    /**
     * 主色变体上的文本。
     */
    val onPrimaryVariant: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onPrimaryVariant

    /**
     * 错误色，用于错误反馈和危险状态。
     */
    val error: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.error

    /**
     * 错误色上的文本。
     */
    val onError: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onError

    /**
     * 错误容器，用于承载错误状态内容。
     */
    val errorContainer: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.errorContainer

    /**
     * 错误容器上的文本。
     */
    val onErrorContainer: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onErrorContainer

    /**
     * 禁用主色，用于开关、按钮和滑块的禁用状态。
     */
    val disabledPrimary: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.disabledPrimary

    /**
     * 禁用主色上的文本。
     */
    val disabledOnPrimary: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.disabledOnPrimary

    /**
     * 按钮禁用主色。
     */
    val disabledPrimaryButton: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.disabledPrimaryButton

    /**
     * 按钮禁用主色上的文本。
     */
    val disabledOnPrimaryButton: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.disabledOnPrimaryButton

    /**
     * 滑块禁用主色。
     */
    val disabledPrimarySlider: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.disabledPrimarySlider

    /**
     * 主色容器。
     */
    val primaryContainer: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.primaryContainer

    /**
     * 主色容器上的文本。
     */
    val onPrimaryContainer: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onPrimaryContainer

    /**
     * 次要色。
     */
    val secondary: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.secondary

    /**
     * 次要色上的文本。
     */
    val onSecondary: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onSecondary

    /**
     * 次要色变体。
     */
    val secondaryVariant: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.secondaryVariant

    /**
     * 次要色变体上的文本。
     */
    val onSecondaryVariant: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onSecondaryVariant

    /**
     * 禁用次要色。
     */
    val disabledSecondary: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.disabledSecondary

    /**
     * 禁用次要色上的文本。
     */
    val disabledOnSecondary: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.disabledOnSecondary

    /**
     * 禁用次要色变体。
     */
    val disabledSecondaryVariant: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.disabledSecondaryVariant

    /**
     * 禁用次要色变体上的文本。
     */
    val disabledOnSecondaryVariant: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.disabledOnSecondaryVariant

    /**
     * 次要色容器。
     */
    val secondaryContainer: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.secondaryContainer

    /**
     * 次要色容器上的文本。
     */
    val onSecondaryContainer: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onSecondaryContainer

    /**
     * 次要色容器变体。
     */
    val secondaryContainerVariant: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.secondaryContainerVariant

    /**
     * 次要色容器变体上的文本。
     */
    val onSecondaryContainerVariant: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onSecondaryContainerVariant

    /**
     * 三级色容器。
     */
    val tertiaryContainer: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.tertiaryContainer

    /**
     * 三级色容器上的文本。
     */
    val onTertiaryContainer: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onTertiaryContainer

    /**
     * 三级色容器变体。
     */
    val tertiaryContainerVariant: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.tertiaryContainerVariant

    /**
     * 应用背景。
     */
    val background: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.background

    /**
     * 背景上的文本。
     */
    val onBackground: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onBackground

    /**
     * 背景上的次级文本。
     */
    val onBackgroundVariant: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onBackgroundVariant

    /**
     * 表面背景。
     */
    val surface: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.surface

    /**
     * 表面上的文本。
     */
    val onSurface: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onSurface

    /**
     * 表面变体。
     */
    val surfaceVariant: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.surfaceVariant

    /**
     * 表面上的次要文本。
     */
    val onSurfaceSecondary: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onSurfaceSecondary

    /**
     * 表面变体上的摘要文本。
     */
    val onSurfaceVariantSummary: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onSurfaceVariantSummary

    /**
     * 表面变体上的操作文本。
     */
    val onSurfaceVariantActions: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onSurfaceVariantActions

    /**
     * 禁用状态下的表面文本。
     */
    val disabledOnSurface: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.disabledOnSurface

    /**
     * 表面容器。
     */
    val surfaceContainer: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.surfaceContainer

    /**
     * 表面容器上的文本。
     */
    val onSurfaceContainer: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onSurfaceContainer

    /**
     * 表面容器上的次级文本。
     */
    val onSurfaceContainerVariant: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onSurfaceContainerVariant

    /**
     * 高对比表面容器。
     */
    val surfaceContainerHigh: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.surfaceContainerHigh

    /**
     * 高对比表面容器上的文本。
     */
    val onSurfaceContainerHigh: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onSurfaceContainerHigh

    /**
     * 最高对比表面容器。
     */
    val surfaceContainerHighest: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.surfaceContainerHighest

    /**
     * 最高对比表面容器上的文本。
     */
    val onSurfaceContainerHighest: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.onSurfaceContainerHighest

    /**
     * 轮廓和边框。
     */
    val outline: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.outline

    /**
     * 分隔线。
     */
    val dividerLine: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.dividerLine

    /**
     * 窗口遮罩，用于对话框和下拉窗口。
     */
    val windowDimming: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.windowDimming

    /**
     * 滑块关键点。
     */
    val sliderKeyPoint: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.sliderKeyPoint

    /**
     * 滑块关键点前景。
     */
    val sliderKeyPointForeground: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.sliderKeyPointForeground

    /**
     * 滑块背景。
     */
    val sliderBackground: Color @Composable @ReadOnlyComposable get() = MiuixTheme.colorScheme.sliderBackground
}
