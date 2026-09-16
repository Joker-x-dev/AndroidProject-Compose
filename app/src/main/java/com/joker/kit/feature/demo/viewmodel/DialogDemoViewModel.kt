package com.joker.kit.feature.demo.viewmodel

import com.joker.kit.R
import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.state.AppDialogAction
import com.joker.kit.core.state.AppDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 全局 Dialog 示例页面 ViewModel。
 *
 * 全部弹窗通过 [AppDialogState] 全局触发，页面自身不维护任何弹窗显示状态。
 */
@HiltViewModel
class DialogDemoViewModel @Inject constructor() : BaseViewModel() {

    /**
     * 展示默认浮层 Dialog。
     *
     * 与 Miuix 官方 `Dialog (O)` 示例一致，横屏时自动切换为居中呈现。
     */
    fun showOverlayDialog() {
        AppDialogState.show(
            title = AppDialogState.resolveString(R.string.dialog_demo_overlay_title),
            summary = AppDialogState.resolveString(R.string.dialog_demo_overlay_dialog_summary),
            actions = dialogActions(),
        )
    }

    /**
     * 展示窗口级 Dialog。
     *
     * 与 Miuix 官方 `Dialog (W)` 示例一致。
     */
    fun showWindowDialog() {
        AppDialogState.show(
            title = AppDialogState.resolveString(R.string.dialog_demo_window_title),
            summary = AppDialogState.resolveString(R.string.dialog_demo_window_dialog_summary),
            actions = dialogActions(),
        )
    }

    /**
     * 展示宽屏双列 Dialog。
     *
     * 竖屏为普通对话框，横屏时标题摘要与按钮分列展示。
     */
    fun showWideDialog() {
        AppDialogState.show(
            title = AppDialogState.resolveString(R.string.dialog_demo_wide_title),
            summary = AppDialogState.resolveString(R.string.dialog_demo_wide_dialog_summary),
            actions = listOf(
                AppDialogAction(
                    text = AppDialogState.resolveString(R.string.dialog_demo_action_allow_once),
                    onClick = { AppDialogState.hide() },
                    isPrimary = true,
                ),
                AppDialogAction(
                    text = AppDialogState.resolveString(R.string.dialog_demo_action_always_allow),
                    onClick = { AppDialogState.hide() },
                ),
                AppDialogAction(
                    text = AppDialogState.resolveString(R.string.dialog_demo_action_deny),
                    onClick = { AppDialogState.hide() },
                ),
            ),
            wide = true,
        )
    }

    /**
     * 展示强制居中 Dialog。
     */
    fun showCenteredDialog() {
        AppDialogState.show(
            title = AppDialogState.resolveString(R.string.dialog_demo_centered_title),
            summary = AppDialogState.resolveString(R.string.dialog_demo_centered_dialog_summary),
            actions = dialogActions(),
            largeScreen = true,
        )
    }

    /**
     * 展示全局确认框。
     */
    fun showConfirmDialog() {
        AppDialogState.confirm(
            title = AppDialogState.resolveString(R.string.dialog_demo_confirm_exit_title),
            summary = AppDialogState.resolveString(R.string.dialog_demo_confirm_exit_summary),
            confirmText = AppDialogState.resolveString(R.string.dialog_demo_action_confirm),
            cancelText = AppDialogState.resolveString(R.string.dialog_demo_action_cancel),
        )
    }

    /**
     * 展示仅确定按钮的全局确认框。
     */
    fun showSingleActionDialog() {
        AppDialogState.confirm(
            title = AppDialogState.resolveString(R.string.dialog_demo_done_title),
            summary = AppDialogState.resolveString(R.string.dialog_demo_done_summary),
            confirmText = AppDialogState.resolveString(R.string.dialog_demo_action_confirm),
            onCancel = null,
        )
    }

    /**
     * 展示自定义按钮文字的全局确认框。
     */
    fun showCustomActionDialog() {
        AppDialogState.confirm(
            title = AppDialogState.resolveString(R.string.dialog_demo_delete_title),
            summary = AppDialogState.resolveString(R.string.dialog_demo_delete_summary),
            confirmText = AppDialogState.resolveString(R.string.dialog_demo_action_delete),
            cancelText = AppDialogState.resolveString(R.string.dialog_demo_action_rethink),
        )
    }

    /**
     * 构造标准对话框按钮。
     *
     * @return 取消与确定按钮列表
     */
    private fun dialogActions(): List<AppDialogAction> {
        return listOf(
            AppDialogAction(
                text = AppDialogState.resolveString(R.string.dialog_demo_action_cancel),
                onClick = { AppDialogState.hide() },
            ),
            AppDialogAction(
                text = AppDialogState.resolveString(R.string.dialog_demo_action_confirm),
                onClick = { AppDialogState.hide() },
                isPrimary = true,
            ),
        )
    }
}
