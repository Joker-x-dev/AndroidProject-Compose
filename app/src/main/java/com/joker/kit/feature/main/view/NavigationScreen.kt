package com.joker.kit.feature.main.view

import androidx.compose.ui.res.stringResource
import com.joker.kit.R

import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.joker.kit.core.annotation.ScreenPreview
import com.joker.kit.core.annotation.ScreenPreviewDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.navigation.demo.DemoResult
import com.joker.kit.feature.main.component.DemoCard
import com.joker.kit.feature.main.component.mainPageContentPadding
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.feature.main.data.DemoCardData
import com.joker.kit.feature.main.model.DemoCardInfo
import com.joker.kit.feature.main.viewmodel.NavigationViewModel
import top.yukonga.miuix.kmp.basic.Card

/**
 * Navigation 页面路由
 *
 * @param bottomInnerPadding 主页面底部导航栏占用高度
 * @param viewModel Navigation 页面 ViewModel
 * @author Joker.X
 */
@Composable
internal fun NavigationRoute(
    bottomInnerPadding: Dp = 0.dp,
    viewModel: NavigationViewModel = hiltViewModel()
) {
    // 收集导航示例卡片列表。
    val cards by viewModel.cards.collectAsState()
    // 收集全局登录状态。
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    // 收集页面回传结果。
    val demoResult by viewModel.demoResult.collectAsState()

    NavigationScreen(
        cards = cards,
        bottomInnerPadding = bottomInnerPadding,
        isLoggedIn = isLoggedIn,
        demoResult = demoResult,
        onCardClick = viewModel::onCardClick
    )
}

/**
 * Navigation 页面
 *
 * @param bottomInnerPadding 主页面底部导航栏占用高度
 * @param cards Demo 卡片列表
 * @param isLoggedIn 是否已登录，登录后展示提示
 * @param demoResult 回传结果
 * @param onCardClick 卡片点击回调
 * @param contentPadding 页面 Scaffold 内容边距
 * @param bottomInnerPadding 主页面底部导航栏占用高度
 * @author Joker.X
 */
@Composable
internal fun NavigationScreen(
    bottomInnerPadding: Dp = 0.dp,
    cards: List<DemoCardInfo> = emptyList(),
    isLoggedIn: Boolean = false,
    demoResult: DemoResult? = null,
    onCardClick: (DemoCardInfo) -> Unit = {}
) {
    AppScaffold(
        titleText = stringResource(R.string.nav_navigation),
        showBackIcon = false,
        contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Horizontal),
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        NavigationContent(
            cards = cards,
            isLoggedIn = isLoggedIn,
            demoResult = demoResult,
            onCardClick = onCardClick,
            contentPadding = paddingValues,
            bottomInnerPadding = bottomInnerPadding,
        )
    }
}

/**
 * Navigation 页面内容
 *
 * @param cards Demo 卡片列表
 * @param isLoggedIn 是否已登录，登录后展示提示
 * @param demoResult 回传结果
 * @param onCardClick 卡片点击回调
 * @author Joker.X
 */
@Composable
private fun NavigationContent(
    cards: List<DemoCardInfo>,
    isLoggedIn: Boolean,
    demoResult: DemoResult?,
    onCardClick: (DemoCardInfo) -> Unit,
    contentPadding: PaddingValues,
    bottomInnerPadding: Dp,
) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = mainPageContentPadding(contentPadding, bottomInnerPadding),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
    ) {
        if (isLoggedIn) {
            item(key = "login-status-nav") {
                LoginStatusBanner()
            }
        }

        demoResult?.let {
            item(key = "demo-result") {
                DemoResultBanner(it)
            }
        }

        item(key = "navigation-cards") {
            Card {
                cards.forEach { info ->
                    DemoCard(info = info, onClick = { onCardClick(info) })
                }
            }
        }
    }
}

/**
 * 登录状态提示卡片
 *
 * @author Joker.X
 */
@Composable
private fun LoginStatusBanner() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(SpacePaddingLarge),
            text = stringResource(R.string.navigation_login_status),
        )
    }
}

/**
 * 回传结果提示卡片
 *
 * @param result 回传结果
 * @author Joker.X
 */
@Composable
private fun DemoResultBanner(result: DemoResult) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(SpacePaddingLarge),
            text = stringResource(R.string.navigation_result_format, result.id, result.message),
        )
    }
}

/**
 * Navigation 页面浅色预览
 *
 * @author Joker.X
 */
@ScreenPreview
@Composable
private fun NavigationPreview() {
    AppTheme {
        NavigationScreen(cards = DemoCardData.navigationCards, isLoggedIn = true)
    }
}

/**
 * Navigation 页面深色预览
 *
 * @author Joker.X
 */
@ScreenPreviewDark
@Composable
private fun NavigationPreviewDark() {
    AppTheme(darkTheme = true) {
        NavigationScreen(cards = DemoCardData.navigationCards, isLoggedIn = true)
    }
}
