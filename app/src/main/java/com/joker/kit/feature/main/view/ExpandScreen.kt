package com.joker.kit.feature.main.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.feature.main.component.DemoCard
import com.joker.kit.feature.main.data.DemoCardData
import com.joker.kit.feature.main.model.DemoCardInfo
import com.joker.kit.feature.main.viewmodel.ExpandViewModel

/**
 * Expand 页面路由
 *
 * @param viewModel Expand 页面 ViewModel
 * @author Joker.X
 */
@Composable
internal fun ExpandRoute(
    viewModel: ExpandViewModel = hiltViewModel()
) {
    val cards by viewModel.cards.collectAsState()

    ExpandScreen(
        cards = cards,
        onCardClick = { info -> info.navigateAction?.invoke() }
    )
}

/**
 * Expand 页面
 *
 * @param cards Demo 卡片列表
 * @param onCardClick 卡片点击回调
 * @author Joker.X
 */
@Composable
internal fun ExpandScreen(
    cards: List<DemoCardInfo> = emptyList(),
    onCardClick: (DemoCardInfo) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(SpacePaddingLarge),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
    ) {
        itemsIndexed(cards) { _, info ->
            DemoCard(
                info = info,
                onClick = { onCardClick(info) }
            )
        }
    }
}

/**
 * Expand 页面浅色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun ExpandPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ExpandScreen(cards = DemoCardData.expandCards)
        }
    }
}

/**
 * Expand 页面深色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun ExpandPreviewDark() {
    AppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            ExpandScreen(cards = DemoCardData.expandCards)
        }
    }
}
