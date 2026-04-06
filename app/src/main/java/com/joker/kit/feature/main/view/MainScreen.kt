package com.joker.kit.feature.main.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.feature.main.component.BottomNavigationBar
import com.joker.kit.feature.main.model.TopLevelDestination
import com.joker.kit.feature.main.viewmodel.MainViewModel

/**
 * 主页面路由
 *
 * @param viewModel 主页面 ViewModel
 * @author Joker.X
 */
@Composable
internal fun MainRoute(
    viewModel: MainViewModel = hiltViewModel()
) {
    val currentPageIndex by viewModel.currentPageIndex.collectAsState()
    MainScreen(
        currentPageIndex = currentPageIndex,
        onNavigationItemSelected = viewModel::updateDestination
    )
}

/**
 * 主页面
 *
 * @param currentPageIndex 当前页面索引
 * @param onNavigationItemSelected 导航项选中回调
 * @author Joker.X
 */
@Composable
internal fun MainScreen(
    currentPageIndex: Int = 0,
    onNavigationItemSelected: (Int) -> Unit = {}
) {
    MainScreenContent(
        currentPageIndex = currentPageIndex,
        onNavigationItemSelected = onNavigationItemSelected
    )
}

/**
 * 主页面内容视图，包含底部导航和内容区域
 *
 * @param currentPageIndex 当前页面索引
 * @param onNavigationItemSelected 导航项选中回调
 * @author Joker.X
 */
@Composable
private fun MainScreenContent(
    currentPageIndex: Int,
    onNavigationItemSelected: (Int) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
        ),
        bottomBar = {
            BottomNavigationBar(
                destinations = TopLevelDestination.entries,
                onNavigateToDestination = onNavigationItemSelected,
                currentPageIndex = currentPageIndex
            )
        }
    ) { innerPadding ->
        MainScreenContentView(
            currentPageIndex = currentPageIndex,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

/**
 * 主页面内容区域
 *
 * @param currentPageIndex 当前页面索引
 * @param modifier 内容区域修饰符
 * @author Joker.X
 */
@Composable
private fun MainScreenContentView(
    currentPageIndex: Int,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (currentPageIndex) {
            0 -> CoreRoute()
            1 -> NavigationRoute()
            2 -> ExpandRoute()
            3 -> AboutRoute()
            else -> CoreRoute()
        }
    }
}

/**
 * 主页面界面浅色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
internal fun MainScreenPreview() {
    AppTheme {
        MainScreen(
            currentPageIndex = 0,
            onNavigationItemSelected = {},
        )
    }
}

/**
 * 主页面界面深色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
internal fun MainScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        MainScreen(
            currentPageIndex = 0,
            onNavigationItemSelected = {}
        )
    }
}
