package com.joker.kit.core.ui.component.snackbar

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.joker.kit.core.state.AppSnackbar
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.SnackbarResult

/**
 * 全局 Snackbar 宿主。
 *
 * 订阅 [AppSnackbar] 消息事件并交由 Miuix [SnackbarHost] 展示。
 * 每条消息使用独立协程展示，多条消息可同时堆叠。
 *
 * @param modifier 宿主修饰符
 * @param bottomPadding 底部额外预留高度，通常由主页面底部导航栏写入
 */
@Composable
fun AppSnackbarHost(
    modifier: Modifier = Modifier,
    bottomPadding: Dp = 0.dp,
) {
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    // 底部安全区取系统导航栏与页面底部导航栏占用高度的较大值。
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val targetBottomPadding = maxOf(navigationBarPadding, bottomPadding)
    // 底部避让高度随页面切换动画过渡，避免提示在导航栏出现或消失时瞬移。
    val resolvedBottomPadding by animateDpAsState(
        targetValue = targetBottomPadding,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "AppSnackbarBottomPadding",
    )

    // 向 AppSnackbar 注册宿主状态，使关闭最旧/最新提示可以作用于当前宿主。
    DisposableEffect(hostState, scope) {
        AppSnackbar.attachHost(hostState, scope)
        onDispose { AppSnackbar.detachHost(hostState) }
    }

    LaunchedEffect(hostState, scope) {
        AppSnackbar.events.collect { message ->
            scope.launch {
                // 操作按钮回调只在用户点击操作按钮后触发。
                val result = hostState.showSnackbar(
                    message = message.message,
                    actionLabel = message.actionLabel,
                    withDismissAction = message.withDismissAction,
                    duration = message.duration,
                )
                if (result == SnackbarResult.ActionPerformed) {
                    message.onAction?.invoke()
                }
                message.onResult?.invoke(result)
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        SnackbarHost(
            state = hostState,
            modifier = Modifier.padding(bottom = resolvedBottomPadding),
        )
    }
}
