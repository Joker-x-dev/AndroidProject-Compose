package com.joker.kit.feature.main.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.core.ui.component.text.TextSize
import com.joker.kit.feature.main.viewmodel.MainTab
import com.joker.kit.feature.main.viewmodel.MainUiState
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
    val uiState by viewModel.uiState.collectAsState()
    MainScreen(
        uiState = uiState,
        onTabSelected = viewModel::selectTab
    )
}

/**
 * 主页面
 *
 * @param uiState UI 状态
 * @param onTabSelected Tab 切换回调
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreen(
    uiState: MainUiState = MainUiState(),
    onTabSelected: (MainTab) -> Unit
) {
    MainScreenContent(
        uiState = uiState,
        onTabSelected = onTabSelected
    )
}

/**
 * 主页面内容视图，包含底部导航和横向 Pager
 *
 * @param uiState UI 状态
 * @param onTabSelected Tab 切换回调
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun MainScreenContent(
    uiState: MainUiState,
    onTabSelected: (MainTab) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { MainTab.allTabs.size })
    val currentPage = pagerState.currentPage

    LaunchedEffect(uiState.currentTab) {
        val targetPage = uiState.currentTab.index
        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    LaunchedEffect(currentPage) {
        val tab = MainTab.fromIndex(currentPage)
        if (tab != uiState.currentTab) {
            onTabSelected(tab)
        }
    }

    Scaffold(
        bottomBar = {
            MainBottomBar(
                tabs = MainTab.allTabs,
                currentTab = uiState.currentTab,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            when (MainTab.fromIndex(page)) {
                MainTab.Core -> CoreDemoRoute()
                MainTab.Navigation -> NavigationDemoRoute()
            }
        }
    }
}

/**
 * 自定义纯文字底部导航栏
 *
 * @param tabs 底部栏 Tab 列表
 * @param currentTab 当前选中 Tab
 * @param onTabSelected Tab 选择回调
 * @author Joker.X
 */
@Composable
private fun MainBottomBar(
    tabs: List<MainTab>,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        tabs.forEach { tab ->
            val selected = tab == currentTab
            AppText(
                text = tab.title,
                size = TextSize.BODY_MEDIUM,
                color = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(tab) }
                    .padding(vertical = 10.dp)
            )
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
            uiState = MainUiState(),
            onTabSelected = {},
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
            uiState = MainUiState(),
            onTabSelected = {}
        )
    }
}
