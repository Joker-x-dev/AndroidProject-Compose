package com.joker.kit.feature.demo.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.R
import com.joker.kit.core.annotation.ScreenPreview
import com.joker.kit.core.annotation.ScreenPreviewDark
import com.joker.kit.core.designsystem.component.SpaceVerticalMedium
import com.joker.kit.core.designsystem.theme.AppColors
import com.joker.kit.core.designsystem.theme.AppTextStyles
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.SpaceHorizontalMedium
import com.joker.kit.core.designsystem.theme.SpacePaddingMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.designsystem.theme.SpacePaddingSmall
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.feature.demo.viewmodel.DialogDemoViewModel
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.preference.ArrowPreference

/**
 * 全局 Dialog 示例路由。
 *
 * @param viewModel Hilt 注入的 DialogDemoViewModel
 */
@Composable
internal fun DialogDemoRoute(
    viewModel: DialogDemoViewModel = hiltViewModel()
) {
    // 弹窗全部由 AppDialogState 全局触发，Route 只绑定事件。
    DialogDemoScreen(
        onShowOverlay = viewModel::showOverlayDialog,
        onShowWindow = viewModel::showWindowDialog,
        onShowWideOverlay = viewModel::showWideDialog,
        onShowWideWindow = viewModel::showWideDialog,
        onShowCentered = viewModel::showCenteredDialog,
        onShowConfirm = viewModel::showConfirmDialog,
        onShowSingleAction = viewModel::showSingleActionDialog,
        onShowCustomAction = viewModel::showCustomActionDialog,
    )
}

/**
 * 全局 Dialog 示例界面。
 *
 * 与 Miuix 官方 Dialog 示例保持一致的入口与呈现效果，
 * 弹窗由全局宿主统一渲染，页面不声明任何弹窗状态。
 *
 * @param onShowOverlay 显示浮层 Dialog
 * @param onShowWindow 显示窗口级 Dialog
 * @param onShowWideOverlay 显示宽屏双列浮层 Dialog
 * @param onShowWideWindow 显示宽屏双列窗口级 Dialog
 * @param onShowCentered 显示强制居中 Dialog
 * @param onShowConfirm 显示全局确认框
 * @param onShowSingleAction 显示仅确定按钮的确认框
 * @param onShowCustomAction 显示自定义按钮文字的确认框
 */
@Composable
internal fun DialogDemoScreen(
    onShowOverlay: () -> Unit = {},
    onShowWindow: () -> Unit = {},
    onShowWideOverlay: () -> Unit = {},
    onShowWideWindow: () -> Unit = {},
    onShowCentered: () -> Unit = {},
    onShowConfirm: () -> Unit = {},
    onShowSingleAction: () -> Unit = {},
    onShowCustomAction: () -> Unit = {},
) {
    AppScaffold(
        titleText = stringResource(R.string.dialog_demo_title),
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        DialogDemoContent(
            contentPadding = paddingValues,
            onShowOverlay = onShowOverlay,
            onShowWindow = onShowWindow,
            onShowWideOverlay = onShowWideOverlay,
            onShowWideWindow = onShowWideWindow,
            onShowCentered = onShowCentered,
            onShowConfirm = onShowConfirm,
            onShowSingleAction = onShowSingleAction,
            onShowCustomAction = onShowCustomAction,
        )
    }
}

/**
 * 全局 Dialog 示例内容。
 *
 * @param contentPadding 页面 Scaffold 内容边距
 * @param onShowOverlay 显示浮层 Dialog
 * @param onShowWindow 显示窗口级 Dialog
 * @param onShowWideOverlay 显示宽屏双列浮层 Dialog
 * @param onShowWideWindow 显示宽屏双列窗口级 Dialog
 * @param onShowCentered 显示强制居中 Dialog
 * @param onShowConfirm 显示全局确认框
 * @param onShowSingleAction 显示仅确定按钮的确认框
 * @param onShowCustomAction 显示自定义按钮文字的确认框
 */
@Composable
private fun DialogDemoContent(
    contentPadding: PaddingValues,
    onShowOverlay: () -> Unit,
    onShowWindow: () -> Unit,
    onShowWideOverlay: () -> Unit,
    onShowWideWindow: () -> Unit,
    onShowCentered: () -> Unit,
    onShowConfirm: () -> Unit,
    onShowSingleAction: () -> Unit,
    onShowCustomAction: () -> Unit,
) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        // 区块标题使用 SmallTitle 默认水平内边距，卡片单独保留 Miuix 12dp 边距。
        pagePadding = PaddingValues(
            top = SpacePaddingMedium,
            bottom = SpacePaddingMedium,
        ),
    ) {
        item {
            SmallTitle(text = stringResource(R.string.dialog_demo_section_title))
        }
        item {
            DialogEntryCard(
                onShowOverlay = onShowOverlay,
                onShowWindow = onShowWindow,
                onShowWideOverlay = onShowWideOverlay,
                onShowWideWindow = onShowWideWindow,
                onShowCentered = onShowCentered,
            )
        }
        item {
            SpaceVerticalMedium()
            SmallTitle(text = stringResource(R.string.dialog_demo_confirm_section_title))
        }
        item {
            DialogConfirmCard(
                onShowConfirm = onShowConfirm,
                onShowSingleAction = onShowSingleAction,
                onShowCustomAction = onShowCustomAction,
            )
        }
    }
}

/**
 * Dialog 入口卡片。
 *
 * @param onShowOverlay 显示浮层 Dialog
 * @param onShowWindow 显示窗口级 Dialog
 * @param onShowWideOverlay 显示宽屏双列浮层 Dialog
 * @param onShowWideWindow 显示宽屏双列窗口级 Dialog
 * @param onShowCentered 显示强制居中 Dialog
 */
@Composable
private fun DialogEntryCard(
    onShowOverlay: () -> Unit,
    onShowWindow: () -> Unit,
    onShowWideOverlay: () -> Unit,
    onShowWideWindow: () -> Unit,
    onShowCentered: () -> Unit,
) {
    Card(
        modifier = Modifier.padding(horizontal = SpaceHorizontalMedium),
    ) {
        ArrowPreference(
            title = stringResource(R.string.dialog_demo_overlay_title),
            summary = stringResource(R.string.dialog_demo_overlay_summary),
            onClick = onShowOverlay,
        )
        ArrowPreference(
            title = stringResource(R.string.dialog_demo_window_title),
            summary = stringResource(R.string.dialog_demo_window_summary),
            onClick = onShowWindow,
        )
        ArrowPreference(
            title = stringResource(R.string.dialog_demo_wide_overlay_title),
            summary = stringResource(R.string.dialog_demo_wide_summary),
            onClick = onShowWideOverlay,
        )
        ArrowPreference(
            title = stringResource(R.string.dialog_demo_wide_window_title),
            summary = stringResource(R.string.dialog_demo_wide_summary),
            onClick = onShowWideWindow,
        )
        ArrowPreference(
            title = stringResource(R.string.dialog_demo_centered_title),
            summary = stringResource(R.string.dialog_demo_centered_summary),
            onClick = onShowCentered,
        )
    }
}

/**
 * 全局确认框卡片。
 *
 * @param onShowConfirm 显示全局确认框
 * @param onShowSingleAction 显示仅确定按钮的确认框
 * @param onShowCustomAction 显示自定义按钮文字的确认框
 */
@Composable
private fun DialogConfirmCard(
    onShowConfirm: () -> Unit,
    onShowSingleAction: () -> Unit,
    onShowCustomAction: () -> Unit,
) {
    Card(
        modifier = Modifier.padding(horizontal = SpaceHorizontalMedium),
    ) {
        Column(
            modifier = Modifier.padding(SpacePaddingSmall),
            verticalArrangement = Arrangement.spacedBy(SpacePaddingSmall),
        ) {
            Text(
                text = stringResource(R.string.dialog_demo_confirm_hint),
                style = AppTextStyles.body2,
                color = AppColors.onSurfaceSecondary,
            )
            Button(
                onClick = onShowConfirm,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.dialog_demo_show_confirm))
            }
            Button(
                onClick = onShowSingleAction,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.dialog_demo_show_single_action))
            }
            Button(
                onClick = onShowCustomAction,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.dialog_demo_show_custom_action))
            }
        }
    }
}

/** Dialog 示例浅色预览。 */
@ScreenPreview
@Composable
private fun DialogDemoPreview() {
    AppTheme { DialogDemoScreen() }
}

/** Dialog 示例深色预览。 */
@ScreenPreviewDark
@Composable
private fun DialogDemoPreviewDark() {
    AppTheme(darkTheme = true) { DialogDemoScreen() }
}
