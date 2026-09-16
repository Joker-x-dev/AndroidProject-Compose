package com.joker.kit.feature.main.component.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.joker.kit.feature.main.model.TopLevelDestination
import com.joker.kit.core.designsystem.theme.AppColors
import top.yukonga.miuix.kmp.basic.NavigationRail
import top.yukonga.miuix.kmp.basic.NavigationRailItem
import top.yukonga.miuix.kmp.basic.NavigationRailValue
import top.yukonga.miuix.kmp.basic.rememberNavigationRailState

/**
 * 主页面大屏侧边导航栏。
 *
 * @param currentPageIndex 当前选中页签索引
 * @param onNavigationItemClick 用户选择页签回调
 */
@Composable
internal fun MainNavigationRail(
    currentPageIndex: Int,
    onNavigationItemClick: (Int) -> Unit,
) {
    val railState = rememberNavigationRailState(
        initialValue = NavigationRailValue.Collapsed,
    )
    NavigationRail(
        state = railState,
        color = AppColors.surface,
    ) {
        TopLevelDestination.entries.forEachIndexed { index, destination ->
            NavigationRailItem(
                selected = currentPageIndex == index,
                onClick = { onNavigationItemClick(index) },
                icon = ImageVector.vectorResource(destination.iconResId),
                label = stringResource(destination.titleResId),
            )
        }
    }
}
