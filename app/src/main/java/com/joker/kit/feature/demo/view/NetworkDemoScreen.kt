package com.joker.kit.feature.demo.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.base.state.BaseNetWorkUiState
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.SpacePaddingMedium
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.network.BaseNetWorkView
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.feature.demo.viewmodel.NetworkDemoViewModel

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
    // 收集 UI 状态
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NetworkDemoScreen(
    uiState: BaseNetWorkUiState<Goods> = BaseNetWorkUiState.Loading,
    onRetry: () -> Unit = {},
) {
    AppScaffold(
        titleText = "Network Demo",
        onBackClick = { navigateBack() },
    ) {
        BaseNetWorkView(
            uiState = uiState,
            onRetry = onRetry
        ) { data ->
            NetworkDemoContent(data = data)
        }
    }
}

/**
 * Network Demo 内容视图
 *
 * @param data 商品数据
 * @author Joker.X
 */
@Composable
private fun NetworkDemoContent(data: Goods) {
    Column(modifier = Modifier.padding(SpacePaddingMedium)) {
        AppText(text = "商品名称：${data.title}")
        AppText(text = "副标题：${data.subTitle ?: "暂无"}")
        AppText(text = "价格：¥${data.price}")
        AppText(text = "已售：${data.sold} 件")
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
