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
import com.joker.kit.feature.main.component.DemoCard
import com.joker.kit.feature.main.component.mainPageContentPadding
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.feature.main.data.DemoCardData
import com.joker.kit.feature.main.model.DemoCardInfo
import com.joker.kit.feature.main.viewmodel.CoreViewModel
import top.yukonga.miuix.kmp.basic.Card

/**
 * Core 页面路由
 *
 * @param bottomInnerPadding 主页面底部导航栏占用高度
 * @param viewModel Core 页面 ViewModel
 * @author Joker.X
 */
@Composable
internal fun CoreRoute(
    bottomInnerPadding: Dp = 0.dp,
    viewModel: CoreViewModel = hiltViewModel()
) {
    // 收集 Core 页面 Demo 卡片列表。
    val cards by viewModel.cards.collectAsState()
    // 收集全局计数器状态。
    val count by viewModel.count.collectAsState()

    CoreScreen(
        cards = cards,
        bottomInnerPadding = bottomInnerPadding,
        counter = count,
        onCardClick = viewModel::onCardClick
    )
}

/**
 * Core 页面
 *
 * @param bottomInnerPadding 主页面底部导航栏占用高度
 * @param cards Demo 卡片列表
 * @param counter 全局计数器值，大于 0 时在列表顶部展示
 * @param onCardClick 卡片点击回调
 * @author Joker.X
 */
@Composable
internal fun CoreScreen(
    bottomInnerPadding: Dp = 0.dp,
    cards: List<DemoCardInfo> = emptyList(),
    counter: Int = 0,
    onCardClick: (DemoCardInfo) -> Unit = {}
) {
    AppScaffold(
        titleText = stringResource(R.string.nav_core),
        showBackIcon = false,
        contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Horizontal),
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        CoreContent(
            cards = cards,
            counter = counter,
            onCardClick = onCardClick,
            contentPadding = paddingValues,
            bottomInnerPadding = bottomInnerPadding,
        )
    }
}

/**
 * Core 页面内容
 *
 * @param cards Demo 卡片列表
 * @param counter 全局计数器值，大于 0 时在列表顶部展示
 * @param onCardClick 卡片点击回调
 * @param contentPadding 页面 Scaffold 内容边距
 * @param bottomInnerPadding 主页面底部导航栏占用高度
 * @author Joker.X
 */
@Composable
private fun CoreContent(
    cards: List<DemoCardInfo>,
    counter: Int,
    onCardClick: (DemoCardInfo) -> Unit,
    contentPadding: PaddingValues,
    bottomInnerPadding: Dp,
) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = mainPageContentPadding(contentPadding, bottomInnerPadding),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
    ) {
        // 大于 0 才展示计数器
        if (counter > 0) {
            item(key = "counter") {
                CounterBanner(counter = counter)
            }
        }

        item(key = "core-cards") {
            Card {
                cards.forEach { info ->
                    DemoCard(info = info, onClick = { onCardClick(info) })
                }
            }
        }
    }
}

/**
 * 主页计数器提示
 *
 * @param counter 当前计数器值
 * @author Joker.X
 */
@Composable
private fun CounterBanner(counter: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(SpacePaddingLarge),
            text = stringResource(R.string.core_counter_format, counter),
        )
    }
}

/**
 * Core 页面浅色预览
 *
 * @author Joker.X
 */
@ScreenPreview
@Composable
private fun CorePreview() {
    AppTheme {
        CoreScreen(cards = DemoCardData.coreCards, counter = 3)
    }
}

/**
 * Core 页面深色预览
 *
 * @author Joker.X
 */
@ScreenPreviewDark
@Composable
private fun CorePreviewDark() {
    AppTheme(darkTheme = true) {
        CoreScreen(cards = DemoCardData.coreCards, counter = 3)
    }
}
