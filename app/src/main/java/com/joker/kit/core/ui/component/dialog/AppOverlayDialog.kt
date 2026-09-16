package com.joker.kit.core.ui.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import top.yukonga.miuix.kmp.layout.DialogDefaults
import top.yukonga.miuix.kmp.overlay.OverlayDialog

/**
 * 应用浮层对话框。
 *
 * 基于 Miuix `OverlayDialog` 的参数透传封装，
 * 必须位于 `AppScaffold` 提供的 `Scaffold` 作用域内。
 *
 * @param show 是否显示
 * @param onDismissRequest 请求关闭回调
 * @param modifier 对话框修饰符
 * @param title 标题，为空时不展示
 * @param summary 摘要，为空时不展示
 * @param titleColor 标题颜色
 * @param summaryColor 摘要颜色
 * @param backgroundColor 背景色
 * @param enableWindowDim 是否启用遮罩
 * @param onDismissFinished 关闭动画完成回调
 * @param outsideMargin 对话框外部边距
 * @param insideMargin 对话框内部边距
 * @param defaultWindowInsetsPadding 是否应用默认窗口插入内边距
 * @param renderInRootScaffold 是否渲染在最外层 Scaffold
 * @param maxWidth 最大宽度
 * @param largeScreen 是否覆盖大屏呈现样式，为空时自动检测
 * @param cornerRadius 圆角，为空时使用 Miuix 默认值
 * @param content 对话框内容
 */
@Composable
fun AppOverlayDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    summary: String? = null,
    titleColor: Color = DialogDefaults.titleColor(),
    summaryColor: Color = DialogDefaults.summaryColor(),
    backgroundColor: Color = DialogDefaults.backgroundColor(),
    enableWindowDim: Boolean = true,
    onDismissFinished: (() -> Unit)? = null,
    outsideMargin: DpSize = DialogDefaults.outsideMargin,
    insideMargin: DpSize = DialogDefaults.insideMargin,
    defaultWindowInsetsPadding: Boolean = true,
    renderInRootScaffold: Boolean = true,
    maxWidth: Dp = DialogDefaults.MaxWidth,
    largeScreen: Boolean? = null,
    cornerRadius: Dp? = null,
    content: @Composable () -> Unit,
) {
    OverlayDialog(
        show = show,
        modifier = modifier,
        title = title,
        titleColor = titleColor,
        summary = summary,
        summaryColor = summaryColor,
        backgroundColor = backgroundColor,
        enableWindowDim = enableWindowDim,
        onDismissRequest = onDismissRequest,
        onDismissFinished = onDismissFinished,
        outsideMargin = outsideMargin,
        insideMargin = insideMargin,
        defaultWindowInsetsPadding = defaultWindowInsetsPadding,
        renderInRootScaffold = renderInRootScaffold,
        maxWidth = maxWidth,
        largeScreen = largeScreen,
        cornerRadius = cornerRadius,
        content = content,
    )
}
