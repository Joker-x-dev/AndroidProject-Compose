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
import com.joker.kit.core.navigation.demo.DemoResult
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.feature.main.component.DemoCard
import com.joker.kit.feature.main.data.DemoCardData
import com.joker.kit.feature.main.model.DemoCardInfo
import com.joker.kit.feature.main.viewmodel.NavigationDemoViewModel

/**
 * Navigation Demo 路由
 *
 * @param viewModel Navigation Demo ViewModel
 * @author Joker.X
 */
@Composable
internal fun NavigationDemoRoute(
    viewModel: NavigationDemoViewModel = hiltViewModel()
) {
    val cards by viewModel.cards.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val demoResult by viewModel.demoResult.collectAsState()

    NavigationDemoScreen(
        cards = cards,
        isLoggedIn = isLoggedIn,
        demoResult = demoResult,
        onCardClick = { info -> info.navigateAction?.invoke() }
    )
}

/**
 * Navigation Demo 界面
 *
 * @param cards Demo 卡片列表
 * @param isLoggedIn 是否已登录，登录后展示提示
 * @param demoResult 回传结果
 * @param onCardClick 卡片点击回调
 * @author Joker.X
 */
@Composable
internal fun NavigationDemoScreen(
    cards: List<DemoCardInfo> = emptyList(),
    isLoggedIn: Boolean = false,
    demoResult: DemoResult? = null,
    onCardClick: (DemoCardInfo) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(SpacePaddingLarge),
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

        itemsIndexed(cards) { _, info ->
            DemoCard(
                info = info,
                onClick = { onCardClick(info) }
            )
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
        AppText(
            modifier = Modifier.padding(SpacePaddingLarge),
            text = "登录状态：已登录",
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
        AppText(
            modifier = Modifier.padding(SpacePaddingLarge),
            text = "回传结果：id=${result.id}，message=${result.message}",
        )
    }
}

/**
 * Navigation Demo 浅色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NavigationDemoPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            NavigationDemoScreen(cards = DemoCardData.navigationCards, isLoggedIn = true)
        }
    }
}

/**
 * Navigation Demo 深色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NavigationDemoPreviewDark() {
    AppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            NavigationDemoScreen(cards = DemoCardData.navigationCards, isLoggedIn = true)
        }
    }
}
