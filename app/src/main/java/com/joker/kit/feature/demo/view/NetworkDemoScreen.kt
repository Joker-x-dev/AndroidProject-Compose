package com.joker.kit.feature.demo.view

import androidx.compose.ui.res.stringResource
import com.joker.kit.R

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.base.state.BaseNetWorkUiState
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.network.BaseNetWorkView
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.feature.demo.viewmodel.NetworkDemoViewModel
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card

/**
 * Network Demo 路由
 *
 * @param viewModel Hilt 注入的 NetworkDemoViewModel
 * @author Joker.X
 */
@Composable
internal fun NetworkDemoRoute(
    viewModel: NetworkDemoViewModel = hiltViewModel()
) {
    // 收集页面请求状态
    val uiState by viewModel.uiState.collectAsState()

    NetworkDemoScreen(
        uiState = uiState,
        onRetry = viewModel::retryRequest
    )
}

/**
 * Network Demo 界面
 *
 * @param uiState UI 状态
 * @param onRetry 重试回调
 * @author Joker.X
 */
@Composable
internal fun NetworkDemoScreen(
    uiState: BaseNetWorkUiState<Goods> = BaseNetWorkUiState.Loading,
    onRetry: () -> Unit = {},
) {
    AppScaffold(
        titleText = stringResource(R.string.network_demo_title),
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        BaseNetWorkView(
            uiState = uiState,
            onRetry = onRetry
        ) { data ->
            NetworkDemoContent(contentPadding = paddingValues, data = data)
        }
    }
}

/**
 * Network Demo 页面内容
 *
 * @param contentPadding 页面脚手架提供的顶部栏和系统栏边距
 * @param data 商品数据
 * @author Joker.X
 */
@Composable
private fun NetworkDemoContent(contentPadding: PaddingValues, data: Goods) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        pagePadding = PaddingValues(SpacePaddingLarge),
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                BasicComponent(title = stringResource(R.string.product_name), summary = data.title)
                BasicComponent(title = stringResource(R.string.product_subtitle), summary = data.subTitle ?: stringResource(R.string.no_content))
                BasicComponent(title = stringResource(R.string.product_price), summary = stringResource(R.string.product_price_format, data.price))
                BasicComponent(title = stringResource(R.string.product_sold), summary = stringResource(R.string.product_sold_format, data.sold))
            }
        }
    }
}

/**
 * Network Demo 界面浅色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NetworkDemoPreview() {
    AppTheme {
        NetworkDemoScreen(uiState = BaseNetWorkUiState.Success(mockGoods()))
    }
}

/**
 * Network Demo 界面深色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NetworkDemoPreviewDark() {
    AppTheme(darkTheme = true) {
        NetworkDemoScreen(uiState = BaseNetWorkUiState.Success(mockGoods()))
    }
}

/**
 * 模拟商品数据
 *
 * @return 示例商品
 * @author Joker.X
 */
private fun mockGoods() = Goods(
    id = 1,
    title = "手机",
    subTitle = "示例副标题",
    mainPic = "",
    price = 199,
    sold = 88,
)
