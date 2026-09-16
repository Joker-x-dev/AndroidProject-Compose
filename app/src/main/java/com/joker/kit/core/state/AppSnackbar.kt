package com.joker.kit.core.state

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.SnackbarDuration
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.SnackbarResult

/**
 * 全局 Snackbar 消息入口。
 *
 * 提供非挂起、非阻塞的触发方式，任意线程与任意业务层均可调用。
 * 消息通过 [events] 交由 MainActivity 中的全局 Snackbar 宿主统一消费和展示，
 * 并支持关闭最旧或最新一条提示。
 */
object AppSnackbar {

    /**
     * 全局 Snackbar 消息载体。
     *
     * @param message 展示文本
     * @param duration 展示时长
     * @param actionLabel 操作按钮文本，为空时不展示操作按钮
     * @param withDismissAction 是否展示关闭按钮
     * @param onAction 操作按钮回调，仅在用户点击操作按钮后触发
     * @param onResult 结束回调，提示消失或操作按钮被点击后触发
     */
    data class Message(
        val message: String,
        val duration: SnackbarDuration,
        val actionLabel: String? = null,
        val withDismissAction: Boolean = false,
        val onAction: (() -> Unit)? = null,
        val onResult: ((SnackbarResult) -> Unit)? = null,
    )

    /** 事件缓冲区容量，超出容量后丢弃最旧消息。 */
    private const val BUFFER_CAPACITY = 64

    /** 内部消息事件流。 */
    private val _events = MutableSharedFlow<Message>(
        extraBufferCapacity = BUFFER_CAPACITY,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    /** 对外暴露的只读消息事件流，供全局宿主订阅。 */
    internal val events: SharedFlow<Message> = _events.asSharedFlow()

    /** 应用上下文，用于解析字符串资源。 */
    @Volatile
    private var applicationContext: Context? = null

    /** 当前活跃的 Snackbar 宿主状态。 */
    @Volatile
    private var hostState: SnackbarHostState? = null

    /** 当前活跃宿主所在的协程作用域，用于执行关闭操作。 */
    @Volatile
    private var hostScope: CoroutineScope? = null

    /**
     * 绑定应用上下文。
     *
     * @param context 应用上下文
     */
    fun initialize(context: Context) {
        applicationContext = context.applicationContext
    }

    /**
     * 注册全局 Snackbar 宿主。
     *
     * 由 [com.joker.kit.core.ui.component.snackbar.AppSnackbarHost] 在进入组合时调用。
     *
     * @param state 宿主状态
     * @param scope 宿主协程作用域
     */
    internal fun attachHost(state: SnackbarHostState, scope: CoroutineScope) {
        hostState = state
        hostScope = scope
    }

    /**
     * 注销全局 Snackbar 宿主。
     *
     * @param state 待注销的宿主状态
     */
    internal fun detachHost(state: SnackbarHostState) {
        if (hostState === state) {
            hostState = null
            hostScope = null
        }
    }

    /**
     * 解析字符串资源。
     *
     * 供需要自行组合提示文案与操作按钮文本的业务层使用。
     *
     * @param resId 字符串资源 ID
     * @return 解析后的文本；上下文未绑定时返回空字符串
     */
    fun resolveString(@StringRes resId: Int): String {
        return applicationContext?.getString(resId).orEmpty()
    }

    /**
     * 展示文本消息。
     *
     * @param message 展示文本
     * @param duration 展示时长，默认短时长
     * @param actionLabel 操作按钮文本，为空时不展示操作按钮
     * @param withDismissAction 是否展示关闭按钮
     * @param onAction 操作按钮回调
     * @param onResult 结束回调，提示消失或操作按钮被点击后触发
     */
    fun show(
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        onAction: (() -> Unit)? = null,
        onResult: ((SnackbarResult) -> Unit)? = null,
    ) {
        _events.tryEmit(
            Message(
                message = message,
                duration = duration,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
                onAction = onAction,
                onResult = onResult,
            )
        )
    }

    /**
     * 展示字符串资源消息。
     *
     * @param resId 字符串资源 ID
     * @param duration 展示时长，默认短时长
     * @param actionLabel 操作按钮文本，为空时不展示操作按钮
     * @param withDismissAction 是否展示关闭按钮
     * @param onAction 操作按钮回调
     * @param onResult 结束回调，提示消失或操作按钮被点击后触发
     */
    fun show(
        @StringRes resId: Int,
        duration: SnackbarDuration = SnackbarDuration.Short,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        onAction: (() -> Unit)? = null,
        onResult: ((SnackbarResult) -> Unit)? = null,
    ) {
        // 上下文未绑定时丢弃消息，避免 Application 初始化阶段抛出异常。
        val context = applicationContext ?: return
        show(
            message = context.getString(resId),
            duration = duration,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            onAction = onAction,
            onResult = onResult,
        )
    }

    /**
     * 展示自定义毫秒时长的文本消息。
     *
     * @param message 展示文本
     * @param durationMillis 展示时长，单位毫秒，必须大于 0
     * @param actionLabel 操作按钮文本，为空时不展示操作按钮
     * @param withDismissAction 是否展示关闭按钮
     * @param onAction 操作按钮回调
     * @param onResult 结束回调，提示消失或操作按钮被点击后触发
     */
    fun showCustom(
        message: String,
        durationMillis: Long,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        onAction: (() -> Unit)? = null,
        onResult: ((SnackbarResult) -> Unit)? = null,
    ) {
        show(
            message = message,
            duration = SnackbarDuration.Custom(durationMillis),
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            onAction = onAction,
            onResult = onResult,
        )
    }

    /**
     * 展示自定义毫秒时长的字符串资源消息。
     *
     * @param resId 字符串资源 ID
     * @param durationMillis 展示时长，单位毫秒，必须大于 0
     * @param actionLabel 操作按钮文本，为空时不展示操作按钮
     * @param withDismissAction 是否展示关闭按钮
     * @param onAction 操作按钮回调
     */
    fun showCustom(
        @StringRes resId: Int,
        durationMillis: Long,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        onAction: (() -> Unit)? = null,
    ) {
        val context = applicationContext ?: return
        showCustom(
            message = context.getString(resId),
            durationMillis = durationMillis,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            onAction = onAction,
        )
    }

    /**
     * 关闭最新一条提示。
     *
     * 宿主未就绪或当前没有提示时按空操作处理。
     */
    fun dismissNewest() {
        val state = hostState ?: return
        val scope = hostScope ?: return
        scope.launch { state.newestSnackbarData()?.dismiss() }
    }

    /**
     * 关闭最旧一条提示。
     *
     * 宿主未就绪或当前没有提示时按空操作处理。
     */
    fun dismissOldest() {
        val state = hostState ?: return
        val scope = hostScope ?: return
        scope.launch { state.oldestSnackbarData()?.dismiss() }
    }
}

/**
 * 全局 Snackbar 底部避让尺寸。
 *
 * 主页面底部导航栏展示时写入实际占用高度，全局 Snackbar 宿主据此抬高，
 * 避免固定、悬浮和液态三种导航样式遮挡提示内容。
 */
object AppSnackbarInsets {

    /** 底部导航栏占用高度。 */
    var bottomBarHeight: Dp by mutableStateOf(0.dp)
}
