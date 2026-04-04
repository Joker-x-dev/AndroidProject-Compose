package com.joker.kit.feature.main.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.feature.main.model.TopLevelDestination

/**
 * 底部导航栏
 *
 * @param destinations 底部导航栏目的地列表
 * @param onNavigateToDestination 点击导航项回调
 * @param currentPageIndex 当前选中页面索引
 * @param modifier 修饰符
 * @author Joker.X
 */
@Composable
internal fun BottomNavigationBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (Int) -> Unit,
    currentPageIndex: Int,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        destinations.forEachIndexed { index, destination ->
            val selected = index == currentPageIndex
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(index) },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.iconResId),
                        contentDescription = destination.titleText,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(text = destination.titleText)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

/**
 * 底部导航栏预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarPreview() {
    AppTheme {
        BottomNavigationBar(
            destinations = TopLevelDestination.entries,
            onNavigateToDestination = {},
            currentPageIndex = 0
        )
    }
}
