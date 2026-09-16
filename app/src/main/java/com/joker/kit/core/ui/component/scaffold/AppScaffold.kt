package com.joker.kit.core.ui.component.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.joker.kit.core.ui.component.appbar.BackButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurDefaults
import top.yukonga.miuix.kmp.blur.isRuntimeShaderSupported
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.blur.rememberLayerBackdrop
import top.yukonga.miuix.kmp.blur.ProgressiveBlur
import top.yukonga.miuix.kmp.blur.progressiveTextureBlur
import top.yukonga.miuix.kmp.blur.textureBlur
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.joker.kit.core.state.LocalAppSettings
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * 当前页面共享的 Miuix 顶部栏滚动行为。
 */
internal val LocalMiuixScrollBehavior = compositionLocalOf<ScrollBehavior?> { null }

/**
 * 应用通用 Miuix 页面骨架。
 *
 * 组件统一承载顶部栏、页面背景、系统栏 Insets、Snackbar、底部栏和浮动操作区域，
 * 并根据应用设置提供顶部栏模糊与顶部栏滚动联动能力。
 *
 * @param modifier 页面骨架根容器修饰符。
 * @param title 顶部栏标题资源 ID；与 [titleText] 同时提供时优先使用 [titleText]。
 * @param titleText 顶部栏运行时标题；为空时不显示标题文本。
 * @param topBarActions 顶部栏右侧操作区域。
 * @param showBackIcon 是否显示统一返回按钮。
 * @param onBackClick 返回按钮点击回调。
 * @param snackbarHostState Snackbar 状态，用于承载页面消息。
 * @param backgroundColor 页面背景色；使用 [Color.Unspecified] 时跟随 Miuix 表面色。
 * @param bottomBar 页面底部栏内容。
 * @param floatingActionButton 页面浮动操作按钮内容。
 * @param topBar 自定义顶部栏；不为空时替代默认 Miuix `TopAppBar`。
 * @param useLargeTopBar 是否启用大标题顶部栏和默认滚动行为。
 * @param blurEnabled 是否允许顶部栏使用模糊效果；最终状态仍受应用设置和运行时 Shader 能力影响。
 * @param blurProgress 顶部栏模糊进度，取值范围会限制在 `0f..1f`。
 * @param topBarColor 顶部栏非模糊状态下的背景色；为空时跟随 Miuix 表面色。
 * @param topBarDefaultWindowInsetsPadding 顶部栏是否自动应用默认窗口 Insets。
 * @param scrollBehavior 顶部栏滚动行为；为空时根据 [useLargeTopBar] 创建默认行为。
 * @param nestedScrollInScaffold 是否在页面根容器注册顶部栏嵌套滚动连接。
 * @param contentWindowInsets 页面内容使用的窗口 Insets，默认合并系统栏和显示缺口 Insets。
 * @param contentShouldConsumePadding 内容是否自行消费 Scaffold 传入的 [PaddingValues]。
 * @param content 页面内容，始终接收 Scaffold 计算出的 [PaddingValues]。
 */
@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    title: Int? = null,
    titleText: String? = null,
    topBarActions: @Composable RowScope.() -> Unit = {},
    showBackIcon: Boolean = true,
    onBackClick: () -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    backgroundColor: Color = Color.Unspecified,
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    topBar: @Composable (() -> Unit)? = null,
    useLargeTopBar: Boolean = true,
    blurEnabled: Boolean = true,
    blurProgress: Float = 1f,
    topBarColor: Color? = null,
    topBarDefaultWindowInsetsPadding: Boolean = true,
    scrollBehavior: ScrollBehavior? = null,
    nestedScrollInScaffold: Boolean = true,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
    contentShouldConsumePadding: Boolean = false,
    content: @Composable (PaddingValues) -> Unit,
) {
    val appSettings = LocalAppSettings.current
    // 标题资源和运行时标题统一解析为顶部栏最终显示文本。
    val titleValue = titleText ?: title?.let { stringResource(it) }.orEmpty()
    // 大标题页面默认启用 Miuix 滚动行为，普通标题页面保持无滚动行为。
    val resolvedScrollBehavior = scrollBehavior ?: if (useLargeTopBar) MiuixScrollBehavior() else null
    val scaffoldModifier = if (nestedScrollInScaffold && resolvedScrollBehavior != null) {
        modifier.nestedScroll(resolvedScrollBehavior.nestedScrollConnection)
    } else {
        modifier
    }
    // 页面默认背景和顶部栏颜色均来自当前 Miuix 主题表面色。
    val surfaceColor = MiuixTheme.colorScheme.surface
    val resolvedBackgroundColor = if (backgroundColor == Color.Unspecified) {
        surfaceColor
    } else {
        backgroundColor
    }
    val backdrop: LayerBackdrop? = if (isRuntimeShaderSupported()) {
        rememberLayerBackdrop {
            drawRect(resolvedBackgroundColor)
            drawContent()
        }
    } else {
        null
    }
    // 模糊效果同时受组件参数、应用设置和进度范围约束。
    val resolvedBlurProgress = if (blurEnabled && appSettings.enableBlur) blurProgress.coerceIn(0f, 1f) else 0f
    val blurActive = resolvedBlurProgress > 0f && backdrop != null
    val resolvedTopBarColor = topBarColor ?: surfaceColor
    CompositionLocalProvider(LocalMiuixScrollBehavior provides resolvedScrollBehavior) {
        Scaffold(
            modifier = scaffoldModifier,
            containerColor = resolvedBackgroundColor,
            contentWindowInsets = contentWindowInsets,
            topBar = {
            val topBarContent: @Composable () -> Unit = {
                if (topBar != null) {
                    topBar()
                } else if (titleValue.isNotEmpty() || showBackIcon) {
                    TopAppBar(
                        title = titleValue,
                        largeTitle = titleValue,
                        color = if (blurActive) Color.Transparent else resolvedTopBarColor,
                        navigationIcon = {
                            if (showBackIcon) {
                                BackButton(onClick = onBackClick)
                            }
                        },
                        actions = topBarActions,
                        scrollBehavior = resolvedScrollBehavior,
                        defaultWindowInsetsPadding = topBarDefaultWindowInsetsPadding,
                    )
                }
            }
            Box {
                if (blurActive) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .graphicsLayer { alpha = resolvedBlurProgress }
                        .then(
                            if (appSettings.topAppBarBlurStyle == 1) {
                                Modifier.progressiveTextureBlur(
                                    backdrop = backdrop,
                                    shape = RectangleShape,
                                    blurRadius = 25f,
                                    gradient = ProgressiveBlur.Top,
                                    colors = BlurDefaults.blurColors(
                                        blendColors = listOf(
                                            BlendColorEntry(color = surfaceColor.copy(alpha = 0.8f)),
                                        ),
                                    ),
                                )
                            } else {
                                Modifier.textureBlur(
                                    backdrop = backdrop,
                                    shape = RectangleShape,
                                    blurRadius = 25f,
                                    colors = BlurDefaults.blurColors(
                                        blendColors = listOf(
                                            BlendColorEntry(color = surfaceColor.copy(alpha = 0.8f)),
                                        ),
                                    ),
                                )
                            },
                        ),
                )
                }
                if (resolvedBlurProgress < 1f) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(resolvedTopBarColor.copy(alpha = 1f - resolvedBlurProgress)),
                )
                }
                topBarContent()
            }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
        ) { paddingValues ->
            // 根容器默认消费 Insets，显式交给内容时保留原始 PaddingValues。
            val bodyModifier = Modifier
                .fillMaxSize()
                .background(resolvedBackgroundColor)
                .then(if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier)
                .then(if (contentShouldConsumePadding) Modifier else Modifier.padding(paddingValues))
            Box(modifier = bodyModifier) {
                content(paddingValues)
            }
        }
    }
}
