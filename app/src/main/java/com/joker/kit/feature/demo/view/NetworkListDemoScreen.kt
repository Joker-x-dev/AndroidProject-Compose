package com.joker.kit.feature.demo.view

import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.base.state.BaseNetWorkListUiState
import com.joker.kit.core.base.state.LoadMoreState
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.ShapeMedium
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.network.BaseNetWorkListView
import com.joker.kit.core.ui.component.refresh.RefreshLayout
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.feature.demo.viewmodel.NetworkListDemoViewModel

/**
 * Network List Demo 路由
 *
 * @param viewModel Hilt 注入的 NetworkListDemoViewModel
 * @author Joker.X
 */
@Composable
internal fun NetworkListDemoRoute(
    viewModel: NetworkListDemoViewModel = hiltViewModel()
) {
    // 收集 ui 状态
    val uiState by viewModel.uiState.collectAsState()
    // 收集列表数据
    val listData by viewModel.listData.collectAsState()
    // 收集刷新状态
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    // 收集加载更多状态
    val loadMoreState by viewModel.loadMoreState.collectAsState()

    NetworkListDemoScreen(
        uiState = uiState,
        list = listData,
        isRefreshing = isRefreshing,
        loadMoreState = loadMoreState,
        onRefresh = viewModel::onRefresh,
        onLoadMore = viewModel::onLoadMore,
        shouldTriggerLoadMore = viewModel::shouldTriggerLoadMore,
        onBackClick = ::navigateBack,
        onRetry = viewModel::retryRequest,
    )
}

/**
 * Network List Demo 界面
 *
 * @param uiState 网络列表 UI 状态
 * @param list 商品列表数据
 * @param isRefreshing 是否正在刷新
 * @param loadMoreState 加载更多状态
 * @param onRefresh 刷新回调
 * @param onLoadMore 加载更多回调
 * @param shouldTriggerLoadMore 是否触发加载更多
 * @param onBackClick 返回回调
 * @param onRetry 重试回调
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NetworkListDemoScreen(
    uiState: BaseNetWorkListUiState = BaseNetWorkListUiState.Loading,
    list: List<Goods> = emptyList(),
    isRefreshing: Boolean = false,
    loadMoreState: LoadMoreState = LoadMoreState.PullToLoad,
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    shouldTriggerLoadMore: (lastIndex: Int, totalCount: Int) -> Boolean = { _, _ -> false },
    onBackClick: () -> Unit = {},
    onRetry: () -> Unit = {},
) {
    AppScaffold(
        titleText = "Network List Demo",
        onBackClick = onBackClick
    ) {
        BaseNetWorkListView(
            uiState = uiState,
            onRetry = onRetry
        ) {
            NetworkListDemoContent(
                list = list,
                isRefreshing = isRefreshing,
                loadMoreState = loadMoreState,
                onRefresh = onRefresh,
                onLoadMore = onLoadMore,
                shouldTriggerLoadMore = shouldTriggerLoadMore
            )
        }
    }
}

/**
 * Network List Demo 内容视图
 *
 * @param list 商品列表数据
 * @param isRefreshing 是否正在刷新
 * @param loadMoreState 加载更多状态
 * @param onRefresh 刷新回调
 * @param onLoadMore 加载更多回调
 * @param shouldTriggerLoadMore 是否触发加载更多
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NetworkListDemoContent(
    list: List<Goods>,
    isRefreshing: Boolean,
    loadMoreState: LoadMoreState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    shouldTriggerLoadMore: (lastIndex: Int, totalCount: Int) -> Boolean
) {
    RefreshLayout(
        isRefreshing = isRefreshing,
        loadMoreState = loadMoreState,
        onRefresh = onRefresh,
        onLoadMore = onLoadMore,
        shouldTriggerLoadMore = shouldTriggerLoadMore
    ) {
        itemsIndexed(list) { _, item ->
            GoodsListItem(goods = item)
        }
    }
}

/**
 * 简单展示商品信息的列表项
 *
 * @param goods 商品实体
 * @author Joker.X
 */
@Composable
private fun GoodsListItem(goods: Goods) {
    ListItem(
        modifier = Modifier.clip(ShapeMedium),
        headlineContent = { AppText(text = goods.title.ifBlank { "未命名商品" }) },
        supportingContent = {
            AppText(text = goods.subTitle?.ifBlank { "暂无描述" } ?: "暂无描述")
        },
        trailingContent = { AppText(text = "¥${goods.price}") },
    )
}

/**
 * Network List Demo 界面浅色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NetworkListDemoPreview() {
    AppTheme {
        NetworkListDemoScreen(
            uiState = BaseNetWorkListUiState.Success,
            list = previewGoodsList(),
            loadMoreState = LoadMoreState.PullToLoad
        )
    }
}

/**
 * Network List Demo 界面深色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NetworkListDemoPreviewDark() {
    AppTheme(darkTheme = true) {
        NetworkListDemoScreen(
            uiState = BaseNetWorkListUiState.Success,
            list = previewGoodsList(),
            loadMoreState = LoadMoreState.PullToLoad
        )
    }
}

/**
 * 预览用商品列表数据
 *
 * @return 商品预览数据列表
 * @author Joker.X
 */
private fun previewGoodsList() = listOf(
    Goods(id = 1, title = "小米手机 14", subTitle = "直屏旗舰", price = 3999, sold = 5000),
    Goods(id = 2, title = "Apple AirPods", subTitle = "二代", price = 1299, sold = 8000),
    Goods(id = 3, title = "Switch OLED", subTitle = "游戏机", price = 2599, sold = 3000),
)
