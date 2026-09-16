package com.joker.kit.feature.demo.viewmodel

import androidx.lifecycle.viewModelScope
import com.joker.kit.R
import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.state.AppSnackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.SnackbarDuration
import top.yukonga.miuix.kmp.basic.SnackbarResult
import javax.inject.Inject

/**
 * Snackbar 示例页面 ViewModel。
 *
 * 按 Miuix 官方 Snackbar 示例触发不同时长、操作按钮与关闭行为，
 * 并记录操作按钮的最终结果。
 */
@HiltViewModel
class SnackbarDemoViewModel @Inject constructor() : BaseViewModel() {

    /** 操作按钮的运行态文案资源 ID，为空时展示默认文案。 */
    private val _actionStateResId = MutableStateFlow<Int?>(null)

    /** 对外暴露的操作按钮运行态文案状态流。 */
    val actionStateResId: StateFlow<Int?> = _actionStateResId.asStateFlow()

    /**
     * 关闭最旧一条提示。
     */
    fun dismissOldest() {
        AppSnackbar.dismissOldest()
    }

    /**
     * 关闭最新一条提示。
     */
    fun dismissNewest() {
        AppSnackbar.dismissNewest()
    }

    /**
     * 展示短提示。
     */
    fun showShort() {
        AppSnackbar.show(
            resId = R.string.snackbar_demo_message_short,
            duration = SnackbarDuration.Short,
        )
    }

    /**
     * 展示长提示。
     */
    fun showLong() {
        AppSnackbar.show(
            resId = R.string.snackbar_demo_message_long,
            duration = SnackbarDuration.Long,
        )
    }

    /**
     * 展示自定义时长提示。
     */
    fun showCustom() {
        AppSnackbar.showCustom(
            resId = R.string.snackbar_demo_message_custom,
            durationMillis = CUSTOM_DURATION_MILLIS,
        )
    }

    /**
     * 展示带操作按钮的提示，并回显操作结果。
     */
    fun showAction() {
        _actionStateResId.value = R.string.snackbar_demo_action_running
        AppSnackbar.show(
            resId = R.string.snackbar_demo_message_action,
            duration = SnackbarDuration.Short,
            actionLabel = AppSnackbar.resolveString(R.string.snackbar_demo_action_label),
            onResult = { result ->
                _actionStateResId.value = when (result) {
                    SnackbarResult.ActionPerformed -> R.string.snackbar_demo_action_undo
                    SnackbarResult.Dismissed -> R.string.snackbar_demo_action_expired
                }
            },
        )
    }

    /**
     * 展示可手动关闭的提示。
     */
    fun showDismissible() {
        AppSnackbar.show(
            resId = R.string.snackbar_demo_message_dismissible,
            duration = SnackbarDuration.Long,
            withDismissAction = true,
        )
    }

    /**
     * 展示常驻提示。
     */
    fun showIndefinite() {
        AppSnackbar.show(
            resId = R.string.snackbar_demo_message_indefinite,
            duration = SnackbarDuration.Indefinite,
            withDismissAction = true,
        )
    }

    /**
     * 连续触发多条提示，演示同时堆叠。
     */
    fun showStacked() {
        viewModelScope.launch {
            AppSnackbar.show(R.string.snackbar_demo_message_stack_one)
            delay(STACK_INTERVAL_MILLIS)
            AppSnackbar.show(R.string.snackbar_demo_message_stack_two)
            delay(STACK_INTERVAL_MILLIS)
            AppSnackbar.show(R.string.snackbar_demo_message_stack_three)
        }
    }

    /** 页面常量。 */
    private companion object {
        /** 相邻堆叠消息触发间隔，单位毫秒。 */
        const val STACK_INTERVAL_MILLIS = 120L

        /** 自定义提示展示时长，单位毫秒。 */
        const val CUSTOM_DURATION_MILLIS = 2_000L
    }
}
