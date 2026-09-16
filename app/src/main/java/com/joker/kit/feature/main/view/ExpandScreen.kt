package com.joker.kit.feature.main.view

import androidx.compose.ui.res.stringResource
import com.joker.kit.R

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.fillMaxSize
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
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.feature.main.component.DemoCard
import com.joker.kit.feature.main.component.mainPageContentPadding
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.feature.main.data.DemoCardData
import com.joker.kit.feature.main.model.DemoCardInfo
import com.joker.kit.feature.main.viewmodel.ExpandViewModel
import top.yukonga.miuix.kmp.basic.Card

/**
 * Expand 页面路由
 *
 * @param bottomInnerPadding 主页面底部导航栏占用高度
 * @param viewModel Expand 页面 ViewModel
 * @author Joker.X
 */
@Composable
internal fun ExpandRoute(
    bottomInnerPadding: Dp = 0.dp,
    viewModel: ExpandViewModel = hiltViewModel()
) {
    // 收集扩展能力卡片列表。
    val cards by viewModel.cards.collectAsState()

    ExpandScreen(
        cards = cards,
        bottomInnerPadding = bottomInnerPadding,
        onCardClick = viewModel::onCardClick
    )
}

/**
 * Expand 页面
 *
 * @param bottomInnerPadding 主页面底部导航栏占用高度
 * @param cards Demo 卡片列表
 * @param onCardClick 卡片点击回调
 * @author Joker.X
 */
@Composable
internal fun ExpandScreen(
    bottomInnerPadding: Dp = 0.dp,
    cards: List<DemoCardInfo> = emptyList(),
    onCardClick: (DemoCardInfo) -> Unit = {}
) {
    AppScaffold(
        titleText = stringResource(R.string.nav_expand),
        showBackIcon = false,
        contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Horizontal),
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        ExpandContent(
            cards = cards,
            onCardClick = onCardClick,
            contentPadding = paddingValues,
            bottomInnerPadding = bottomInnerPadding,
        )
    }
}

/**
 * Expand 页面内容
 *
 * @param cards Demo 卡片列表
 * @param onCardClick 卡片点击回调
 * @param contentPadding 页面 Scaffold 内容边距
 * @param bottomInnerPadding 主页面底部导航栏占用高度
 * @author Joker.X
 */
@Composable
private fun ExpandContent(
    cards: List<DemoCardInfo>,
    onCardClick: (DemoCardInfo) -> Unit,
    contentPadding: PaddingValues,
    bottomInnerPadding: Dp,
) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = mainPageContentPadding(contentPadding, bottomInnerPadding),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
    ) {
        item(key = "expand-cards") {
            Card {
                cards.forEach { info ->
                    DemoCard(info = info, onClick = { onCardClick(info) })
                }
            }
        }
    }
}

/**
 * Expand 页面浅色预览
 *
 * @author Joker.X
 */
@ScreenPreview
@Composable
private fun ExpandPreview() {
    AppTheme {
        ExpandScreen(cards = DemoCardData.expandCards)
    }
}

/**
 * Expand 页面深色预览
 *
 * @author Joker.X
 */
@ScreenPreviewDark
@Composable
private fun ExpandPreviewDark() {
    AppTheme(darkTheme = true) {
        ExpandScreen(cards = DemoCardData.expandCards)
    }
}
