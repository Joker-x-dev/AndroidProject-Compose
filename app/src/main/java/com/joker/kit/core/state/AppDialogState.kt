package com.joker.kit.core.state

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import top.yukonga.miuix.kmp.layout.DialogDefaults

/**
 * 对话框按钮配置。
 *
 * @param text 按钮文字
 * @param onClick 点击回调
 * @param isPrimary 是否使用主色按钮
 * @param color 按钮颜色覆盖值，为空时使用对应按钮样式的默认颜色
 */
@Immutable
data class AppDialogAction(
    val text: String,
    val onClick: () -> Unit,
    val isPrimary: Boolean = false,
    val color: Color? = null,
)

/**
 * 全局对话框状态。
 *
 * 基于 Miuix Dialog 封装的简化入口，参数命名与 Miuix 保持一致。
 * 通过 [config] 驱动 MainActivity 中的全局宿主渲染，任意线程与任意业务层一行代码即可触发：
 *
 * ```kotlin
 * // 确认框
 * AppDialogState.confirm(
 *     title = "确认退出",
 *     summary = "确定要退出登录吗？",
 *     onConfirm = { logout() },
 * )
 *
 * // 带自定义按钮的对话框
 * AppDialogState.show(
 *     title = "Dialog",
 *     summary = "点击按钮关闭对话框。",
 *     actions = listOf(
 *         AppDialogAction(text = "取消", onClick = { AppDialogState.hide() }),
 *         AppDialogAction(text = "确定", onClick = { AppDialogState.hide() }, isPrimary = true),
 *     ),
 * )
 * ```
 */
@Stable
object AppDialogState {

    /**
     * 对话框配置。
     *
     * @param title 标题，为空时不展示
     * @param summary 内容摘要，为空时不展示
     * @param actions 操作按钮列表
     * @param maxWidth 最大宽度
     * @param wide 是否启用宽屏双列布局，横屏时标题摘要与按钮分列展示
     * @param largeScreen 是否覆盖大屏呈现样式，为空时自动检测
     * @param dismissOnClickOutside 是否允许点击遮罩或返回键关闭
     * @param onDismissRequest 关闭回调，为空时不额外处理
     */
    data class Config(
        val title: String? = null,
        val summary: String? = null,
        val actions: List<AppDialogAction> = emptyList(),
        val maxWidth: Dp = DialogDefaults.MaxWidth,
        val wide: Boolean = false,
        val largeScreen: Boolean? = null,
        val dismissOnClickOutside: Boolean = true,
        val onDismissRequest: (() -> Unit)? = null,
    )

    /** 宽屏双列布局使用的最大宽度。 */
    private val WideMaxWidth = 560.dp

    private val _config = MutableStateFlow<Config?>(null)

    /** 对外暴露的只读对话框配置。 */
    val config: StateFlow<Config?> = _config.asStateFlow()

    /** 应用上下文，用于解析字符串资源。 */
    @Volatile
    private var applicationContext: Context? = null

    /**
     * 绑定应用上下文。
     *
     * @param context 应用上下文
     */
    fun initialize(context: Context) {
        applicationContext = context.applicationContext
    }

    /**
     * 解析字符串资源。
     *
     * 供需要自行组合弹窗文案的业务层使用。
     *
     * @param resId 字符串资源 ID
     * @return 解析后的文本；上下文未绑定时返回空字符串
     */
    fun resolveString(@StringRes resId: Int): String {
        return applicationContext?.getString(resId).orEmpty()
    }

    /**
     * 展示对话框。
     *
     * @param title 标题，为空时不展示
     * @param summary 内容摘要，为空时不展示
     * @param actions 操作按钮列表
     * @param maxWidth 最大宽度
     * @param wide 是否启用宽屏双列布局
     * @param largeScreen 是否覆盖大屏呈现样式，为空时自动检测
     * @param dismissOnClickOutside 是否允许点击遮罩或返回键关闭
     * @param onDismissRequest 关闭回调
     */
    fun show(
        title: String? = null,
        summary: String? = null,
        actions: List<AppDialogAction> = emptyList(),
        maxWidth: Dp = DialogDefaults.MaxWidth,
        wide: Boolean = false,
        largeScreen: Boolean? = null,
        dismissOnClickOutside: Boolean = true,
        onDismissRequest: (() -> Unit)? = null,
    ) {
        _config.value = Config(
            title = title,
            summary = summary,
            actions = actions,
            maxWidth = if (wide) WideMaxWidth else maxWidth,
            wide = wide,
            largeScreen = largeScreen,
            dismissOnClickOutside = dismissOnClickOutside,
            onDismissRequest = onDismissRequest,
        )
    }

    /**
     * 展示确认对话框。
     *
     * @param title 标题
     * @param summary 内容摘要，为空时不展示
     * @param confirmText 确定按钮文字
     * @param cancelText 取消按钮文字
     * @param confirmColor 确定按钮颜色，为空时使用主题主色
     * @param dismissOnClickOutside 是否允许点击遮罩或返回键关闭
     * @param onConfirm 确定回调
     * @param onCancel 取消回调，为 null 时不展示取消按钮
     */
    fun confirm(
        title: String = DEFAULT_TITLE,
        summary: String? = null,
        confirmText: String = DEFAULT_CONFIRM_TEXT,
        cancelText: String = DEFAULT_CANCEL_TEXT,
        confirmColor: Color? = null,
        dismissOnClickOutside: Boolean = true,
        onConfirm: () -> Unit = {},
        onCancel: (() -> Unit)? = {},
    ) {
        val actions = buildList {
            onCancel?.let { cancel ->
                add(
                    AppDialogAction(
                        text = cancelText,
                        onClick = {
                            hide()
                            cancel.invoke()
                        },
                    )
                )
            }
            add(
                AppDialogAction(
                    text = confirmText,
                    onClick = {
                        hide()
                        onConfirm.invoke()
                    },
                    isPrimary = true,
                    color = confirmColor,
                )
            )
        }
        show(
            title = title,
            summary = summary,
            actions = actions,
            dismissOnClickOutside = dismissOnClickOutside,
        )
    }

    /**
     * 隐藏对话框。
     */
    fun hide() {
        _config.value = null
    }

    /** 默认标题。 */
    private const val DEFAULT_TITLE = "提示"

    /** 默认确定按钮文字。 */
    private const val DEFAULT_CONFIRM_TEXT = "确定"

    /** 默认取消按钮文字。 */
    private const val DEFAULT_CANCEL_TEXT = "取消"
}
