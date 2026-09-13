package com.joker.kit.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 文字样式访问入口
 */
object AppTextStyles {
    /**
     * 主文本样式。
     */
    val main: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.main

    /**
     * 段落样式。
     */
    val paragraph: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.paragraph

    /**
     * 一级正文样式。
     */
    val body1: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.body1

    /**
     * 二级正文样式。
     */
    val body2: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.body2

    /**
     * 按钮文本样式。
     */
    val button: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.button

    /**
     * 一级脚注样式。
     */
    val footnote1: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.footnote1

    /**
     * 二级脚注样式。
     */
    val footnote2: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.footnote2

    /**
     * 一级标题样式。
     */
    val title1: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.title1

    /**
     * 二级标题样式。
     */
    val title2: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.title2

    /**
     * 三级标题样式。
     */
    val title3: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.title3

    /**
     * 四级标题样式。
     */
    val title4: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.title4

    /**
     * 一级标题栏样式。
     */
    val headline1: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.headline1

    /**
     * 二级标题栏样式。
     */
    val headline2: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.headline2

    /**
     * 副标题样式。
     */
    val subtitle: TextStyle @Composable @ReadOnlyComposable get() = MiuixTheme.textStyles.subtitle
}
