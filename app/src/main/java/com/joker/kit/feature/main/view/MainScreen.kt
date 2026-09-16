package com.joker.kit.feature.main.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.annotation.ScreenPreviewMultiDevice
import com.joker.kit.core.annotation.ScreenPreviewMultiDeviceDark
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.AppColors
import com.joker.kit.core.state.AppSettingsUiState
import com.joker.kit.core.state.AppSnackbarInsets
import com.joker.kit.core.ui.adaptive.bp
import com.joker.kit.feature.main.component.navigation.BottomNavigationBar
import com.joker.kit.feature.main.component.navigation.MainNavigationRail
import com.joker.kit.feature.main.component.navigation.MainPagerContent
import com.joker.kit.feature.main.component.navigation.MainPagerState
import com.joker.kit.feature.main.component.navigation.rememberMainPagerState
import com.joker.kit.feature.main.model.TopLevelDestination
import com.joker.kit.feature.main.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.snapshotFlow
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.rememberLayerBackdrop

/**
 * 主页面路由入口。
 *
 * @param viewModel 主页面 ViewModel
 */
@Composable
internal fun MainRoute(
    viewModel: MainViewModel = hiltViewModel(),
) {
    // 收集主页面当前选中的导航页签。
    val selectedPage by viewModel.currentPageIndex.collectAsState()
    // 收集导航栏样式、显示开关和页面滚动配置。
    val settings by viewModel.settings.collectAsState()
    MainScreen(
        selectedPage = selectedPage,
        settings = settings,
        onNavigationItemClick = viewModel::onNavigationItemClick,
        onPageSettled = viewModel::onPageSettled,
    )
}

/**
 * 主页面骨架，负责准备 Pager 状态和全局页面配置。
 *
 * @param selectedPage ViewModel 当前选中页签索引
 * @param settings 应用设置状态
 * @param onNavigationItemClick 用户选择页签回调
 * @param onPageSettled Pager 完成滚动后的页签同步回调
 */
@Composable
internal fun MainScreen(
    selectedPage: Int = 0,
    settings: AppSettingsUiState = AppSettingsUiState(),
    onNavigationItemClick: (Int) -> Unit = {},
    onPageSettled: (Int) -> Unit = {},
) {
    // 顶层导航目的地数量决定 Pager 的合法页索引范围。
    val pageCount = TopLevelDestination.entries.size
    // Pager 状态负责记录当前页面位置和手势滚动进度。
    val pagerState = rememberPagerState(
        initialPage = selectedPage.coerceIn(0, pageCount - 1),
        pageCount = { pageCount },
    )
    // MD/LG 断点始终使用左侧 Rail，XS/SM 断点使用底部导航栏。
    val useNavigationRail = bp(
        sm = false,
        md = true,
        lg = true,
        defaultValue = false,
    )
    // Pager 动画控制器隔离 Compose 滚动细节，页面只触发目标页变化。
    val mainPagerState = rememberMainPagerState(
        pagerState = pagerState,
        animatePageChanges = !useNavigationRail,
    )
    // 导航栏模糊背景使用当前主题表面色作为底图。
    val surfaceColor = AppColors.surface
    // 背景捕获对象供底部导航栏和 Pager 内容复用。
    val backdrop = rememberLayerBackdrop {
        drawRect(surfaceColor)
        drawContent()
    }

    MainContent(
        settings = settings,
        pageCount = pageCount,
        pagerState = pagerState,
        mainPagerState = mainPagerState,
        useNavigationRail = useNavigationRail,
        backdrop = backdrop,
        selectedPage = selectedPage,
        onNavigationItemClick = onNavigationItemClick,
        onPageSettled = onPageSettled,
    )
}

/**
 * 主页面内容，负责组合 Pager、底部导航栏和大屏侧边栏。
 *
 * @param settings 应用设置状态
 * @param pageCount 主页面数量
 * @param pagerState Pager 状态
 * @param mainPagerState 主页面导航状态
 * @param useNavigationRail 是否使用大屏侧边栏
 * @param backdrop 页面背景捕获对象
 * @param selectedPage ViewModel 当前选中页签
 * @param onNavigationItemClick 用户选择页签回调
 * @param onPageSettled Pager 完成滚动后的页签同步回调
 */
@Composable
private fun MainContent(
    settings: AppSettingsUiState,
    pageCount: Int,
    pagerState: PagerState,
    mainPagerState: MainPagerState,
    useNavigationRail: Boolean,
    backdrop: LayerBackdrop,
    selectedPage: Int,
    onNavigationItemClick: (Int) -> Unit,
    onPageSettled: (Int) -> Unit,
) {
    // ViewModel 的目标页签变化驱动 Pager 动画控制器。
    LaunchedEffect(selectedPage) {
        mainPagerState.animateToPage(selectedPage)
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collectLatest { page ->
                // 程序动画过程中忽略中间页，用户手势跨页时立即同步导航栏选中项。
                if (!mainPagerState.isProgrammaticNavigationActive) {
                    onPageSettled(page)
                }
            }
    }

    // 根据 Pager 页索引渲染对应业务 Route，并统一传递底部安全间距。
    val renderContent: @Composable (Dp) -> Unit = { bottomInnerPadding ->
        MainPagerContent(
            pagerState = pagerState,
            userScrollEnabled = true,
            pageCount = pageCount,
            backdrop = backdrop,
        ) { page ->
            when (page) {
                0 -> CoreRoute(bottomInnerPadding = bottomInnerPadding)
                1 -> NavigationRoute(bottomInnerPadding = bottomInnerPadding)
                2 -> ExpandRoute(bottomInnerPadding = bottomInnerPadding)
                3 -> SettingRoute(bottomInnerPadding = bottomInnerPadding)
            }
        }
    }

    if (useNavigationRail) {
        Scaffold { _ ->
            // 侧边导航模式只消费系统栏起始侧的安全区。
            val startInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout)
                .only(WindowInsetsSides.Start)
            // 侧边导航模式下，底部系统栏高度仍需传给页面内容。
            val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
            Row(modifier = Modifier.fillMaxSize()) {
                MainNavigationRail(
                    currentPageIndex = selectedPage,
                    onNavigationItemClick = onNavigationItemClick,
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .consumeWindowInsets(startInsets),
                ) {
                    renderContent(bottomPadding)
                }
            }
        }
    } else {
        // 底栏高度按像素测量后换算为 dp，供全局 Snackbar 宿主避让。
        val density = LocalDensity.current
        // 底部导航模式将导航栏作为 Scaffold 的底部插槽渲染。
        val bottomBar: @Composable () -> Unit = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { size ->
                        AppSnackbarInsets.bottomBarHeight =
                            with(density) { size.height.toDp() }
                    },
            ) {
                BottomNavigationBar(
                    destinations = TopLevelDestination.entries.toList(),
                    onNavigateToDestination = onNavigationItemClick,
                    currentPageIndex = selectedPage,
                    navigationBarStyle = settings.navigationBarStyle,
                    navigationBarMode = settings.navigationBarMode,
                    floatingNavigationBarStyle = settings.floatingNavigationBarStyle,
                    floatingNavigationBarPosition = settings.floatingNavigationBarPosition,
                    showNavigationBar = true,
                    backdrop = if (settings.enableBlur) backdrop else null,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        }
        // 离开主页面时重置预留高度，避免全局 Snackbar 在无底栏页面留白。
        DisposableEffect(Unit) {
            onDispose {
                AppSnackbarInsets.bottomBarHeight = 0.dp
            }
        }
        Scaffold(bottomBar = bottomBar) { innerPadding ->
            renderContent(innerPadding.calculateBottomPadding())
        }
    }
}

/** 主页面浅色预览。 */
@ScreenPreviewMultiDevice
@Composable
private fun MainScreenPreview() {
    AppTheme { MainScreen() }
}

/** 主页面深色预览。 */
@ScreenPreviewMultiDeviceDark
@Composable
private fun MainScreenPreviewDark() {
    AppTheme(darkTheme = true) { MainScreen() }
}
