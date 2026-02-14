package com.joker.kit.feature.main.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
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
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.feature.main.component.DemoCard
import com.joker.kit.feature.main.data.DemoCardData
import com.joker.kit.feature.main.model.DemoCardInfo
import com.joker.kit.feature.main.viewmodel.CoreDemoViewModel

/**
 * Core Demo 路由
 *
 * @param viewModel Core Demo ViewModel
 * @author Joker.X
 */
@Composable
internal fun CoreDemoRoute(
    viewModel: CoreDemoViewModel = hiltViewModel()
) {
    val cards by viewModel.cards.collectAsState()
    val count by viewModel.count.collectAsState()
    CoreDemoScreen(
        cards = cards,
        counter = count,
        onCardClick = { info -> info.navigateAction?.invoke() }
    )
}

/**
 * Core Demo 界面
 *
 * @param cards Demo 卡片列表
 * @param counter 全局计数器值，大于 0 时在列表顶部展示
 * @param onCardClick 卡片点击回调
 * @author Joker.X
 */
@Composable
internal fun CoreDemoScreen(
    cards: List<DemoCardInfo> = emptyList(),
    counter: Int = 0,
    onCardClick: (DemoCardInfo) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(SpacePaddingLarge),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
    ) {
        // 大于 0 才展示计数器
        if (counter > 0) {
            item(key = "counter") {
                CounterBanner(counter = counter)
            }
        }

        itemsIndexed(cards) { _, info ->
            DemoCard(
                info = info,
                onClick = { onCardClick(info) }
            )
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
        AppText(
            modifier = Modifier.padding(SpacePaddingLarge),
            text = "全局计数器：$counter",
        )
    }
}

/**
 * Core Demo 浅色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun CoreDemoPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CoreDemoScreen(cards = DemoCardData.coreCards, counter = 3)
        }
    }
}

/**
 * Core Demo 深色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun CoreDemoPreviewDark() {
    AppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            CoreDemoScreen(cards = DemoCardData.coreCards, counter = 3)
        }
    }
}
