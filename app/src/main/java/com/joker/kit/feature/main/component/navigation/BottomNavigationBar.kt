package com.joker.kit.feature.main.component.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.AppColors
import com.joker.kit.feature.main.component.navigation.liquid.IosLiquidGlassNavigationBar
import com.joker.kit.feature.main.model.TopLevelDestination
import top.yukonga.miuix.kmp.basic.FloatingNavigationBar
import top.yukonga.miuix.kmp.basic.FloatingNavigationBarItem
import top.yukonga.miuix.kmp.basic.NavigationBar
import top.yukonga.miuix.kmp.basic.NavigationBarDisplayMode
import top.yukonga.miuix.kmp.basic.NavigationBarItem
import top.yukonga.miuix.kmp.basic.NavigationItem
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurDefaults
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.textureBlur

/**
 * Main 模块底部导航栏。
 *
 * 组件负责 Main 模块的导航目的地渲染，视觉效果直接采用 Miuix 官方导航组件和液态玻璃实现。
 *
 * @param destinations Main 模块导航目的地
 * @param onNavigateToDestination 导航项点击回调
 * @param currentPageIndex 当前选中索引
 * @param navigationBarStyle 固定或悬浮模式
 * @param navigationBarMode 固定导航栏显示模式
 * @param floatingNavigationBarStyle 悬浮导航栏样式
 * @param floatingNavigationBarPosition 悬浮导航栏位置
 * @param showNavigationBar 是否显示导航栏
 * @param modifier 组件修饰符
 * @param backdrop 背景捕获对象
 */
@Composable
internal fun BottomNavigationBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (Int) -> Unit,
    currentPageIndex: Int,
    navigationBarStyle: Int = 0,
    navigationBarMode: Int = 0,
    floatingNavigationBarStyle: Int = 0,
    floatingNavigationBarPosition: Int = 0,
    showNavigationBar: Boolean = true,
    modifier: Modifier = Modifier,
    backdrop: LayerBackdrop? = null,
) {
    AnimatedVisibility(
        visible = showNavigationBar,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
    if (navigationBarStyle == 0) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .then(
                    if (backdrop != null) Modifier.textureBlur(
                        backdrop = backdrop,
                        shape = RectangleShape,
                        blurRadius = 25f,
                        colors = BlurDefaults.blurColors(
                            blendColors = listOf(
                                BlendColorEntry(color = AppColors.surface.copy(alpha = 0.8f)),
                            ),
                        ),
                    ) else Modifier,
                )
                .background(
                    if (backdrop != null) Color.Transparent else AppColors.surface,
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                ),
        ) {
            NavigationBar(
                modifier = Modifier,
                color = Color.Transparent,
                mode = NavigationBarDisplayMode.entries.getOrElse(navigationBarMode) {
                    NavigationBarDisplayMode.entries.first()
                },
            ) {
                destinations.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = index == currentPageIndex,
                        onClick = { onNavigateToDestination(index) },
                        icon = ImageVector.vectorResource(destination.iconResId),
                        label = stringResource(destination.titleResId),
                    )
                }
            }
        }
        return@AnimatedVisibility
    }

    val liquidStyle = floatingNavigationBarStyle == 1
    if (liquidStyle) {
        // 悬浮导航栏定位与内容布局分离。
        Box(modifier = modifier) {
            IosLiquidGlassNavigationBar(
                items = destinations.map { destination ->
                    NavigationItem(
                        label = stringResource(destination.titleResId),
                        icon = ImageVector.vectorResource(destination.iconResId),
                    )
                },
                selectedIndex = currentPageIndex,
                onItemClick = onNavigateToDestination,
                backdrop = backdrop,
                isBlurActive = backdrop != null,
            )
        }
        return@AnimatedVisibility
    }
    // 悬浮导航栏保持独立定位层。
    Box(modifier = modifier) {
        FloatingNavigationBar(
            modifier = if (backdrop != null) Modifier.textureBlur(
                backdrop = backdrop,
                shape = RoundedCornerShape(28.dp),
                blurRadius = 25f,
                colors = BlurDefaults.blurColors(
                    blendColors = listOf(
                        BlendColorEntry(
                            color = AppColors.surfaceContainer.copy(alpha = 0.6f),
                        ),
                    ),
                ),
                highlight = null,
            ) else Modifier,
            color = if (backdrop != null) {
                Color.Transparent
            } else {
                AppColors.surfaceContainer
            },
            showDivider = false,
            horizontalAlignment = when (floatingNavigationBarPosition) {
                1 -> Alignment.Start
                2 -> Alignment.End
                else -> Alignment.CenterHorizontally
            },
        ) {
            destinations.forEachIndexed { index, destination ->
                FloatingNavigationBarItem(
                    selected = index == currentPageIndex,
                    onClick = { onNavigateToDestination(index) },
                    icon = ImageVector.vectorResource(destination.iconResId),
                        label = stringResource(destination.titleResId),
                )
            }
        }
    }
    }
}

/** 固定导航栏的图标和文字模式预览。 */
@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarTextPreview() {
    AppTheme {
        BottomNavigationBar(
            destinations = TopLevelDestination.entries,
            onNavigateToDestination = {},
            currentPageIndex = 0,
            navigationBarMode = 0,
        )
    }
}

/** 固定导航栏的仅图标模式预览。 */
@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarIconPreview() {
    AppTheme {
        BottomNavigationBar(
            destinations = TopLevelDestination.entries,
            onNavigateToDestination = {},
            currentPageIndex = 1,
            navigationBarMode = 1,
        )
    }
}

/** 固定导航栏的仅显示选中标签模式预览。 */
@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarSelectedLabelPreview() {
    AppTheme {
        BottomNavigationBar(
            destinations = TopLevelDestination.entries,
            onNavigateToDestination = {},
            currentPageIndex = 2,
            navigationBarMode = 2,
        )
    }
}

/** 普通悬浮导航栏预览。 */
@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarFloatingPreview() {
    AppTheme {
        BottomNavigationBar(
            destinations = TopLevelDestination.entries,
            onNavigateToDestination = {},
            currentPageIndex = 0,
            navigationBarStyle = 1,
            floatingNavigationBarStyle = 0,
            floatingNavigationBarPosition = 0,
        )
    }
}

/** 左侧悬浮导航栏预览。 */
@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarFloatingStartPreview() {
    AppTheme {
        BottomNavigationBar(
            destinations = TopLevelDestination.entries,
            onNavigateToDestination = {},
            currentPageIndex = 0,
            navigationBarStyle = 1,
            floatingNavigationBarPosition = 1,
        )
    }
}

/** 右侧悬浮导航栏预览。 */
@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarFloatingEndPreview() {
    AppTheme {
        BottomNavigationBar(
            destinations = TopLevelDestination.entries,
            onNavigateToDestination = {},
            currentPageIndex = 0,
            navigationBarStyle = 1,
            floatingNavigationBarPosition = 2,
        )
    }
}

/** 液态玻璃悬浮导航栏配置预览。 */
@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarLiquidPreview() {
    AppTheme {
        BottomNavigationBar(
            destinations = TopLevelDestination.entries,
            onNavigateToDestination = {},
            currentPageIndex = 3,
            navigationBarStyle = 1,
            floatingNavigationBarStyle = 1,
        )
    }
}

/** 深色主题下的固定导航栏预览。 */
@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarDarkPreview() {
    AppTheme(darkTheme = true) {
        BottomNavigationBar(
            destinations = TopLevelDestination.entries,
            onNavigateToDestination = {},
            currentPageIndex = 0,
            navigationBarMode = 0,
        )
    }
}
