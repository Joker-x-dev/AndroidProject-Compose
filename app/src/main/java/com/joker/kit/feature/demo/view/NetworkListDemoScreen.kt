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
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.ShapeMedium
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.network.BaseNetWorkListView
import com.joker.kit.core.ui.component.refresh.RefreshLayout
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.feature.demo.skeleton.NetworkListLoadingSkeleton
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
    // 收集页面状态
    val uiState by viewModel.uiState.collectAsState()
    // 收集列表数据
    val listData by viewModel.listData.collectAsState()
    // 收集下拉刷新状态
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    // 收集上拉加载状态
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    // 收集是否还有更多数据
    val hasMoreData by viewModel.hasMoreData.collectAsState()

    NetworkListDemoScreen(
        uiState = uiState,
        list = listData,
        isRefreshing = isRefreshing,
        isLoadingMore = isLoadingMore,
        hasMoreData = hasMoreData,
        onRefresh = viewModel::onRefresh,
        onLoadMore = viewModel::onLoadMore,
        onRetry = viewModel::retryRequest,
    )
}

/**
 * Network List Demo 界面
 *
 * @param uiState 列表页状态
 * @param list 商品列表数据
 * @param isRefreshing 是否正在刷新
 * @param isLoadingMore 是否正在加载更多
 * @param hasMoreData 是否还有更多数据
 * @param onRefresh 刷新回调
 * @param onLoadMore 加载更多回调
 * @param onRetry 重试回调
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NetworkListDemoScreen(
    uiState: BaseNetWorkListUiState = BaseNetWorkListUiState.Loading,
    list: List<Goods> = emptyList(),
    isRefreshing: Boolean = false,
    isLoadingMore: Boolean = false,
    hasMoreData: Boolean = false,
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    onRetry: () -> Unit = {},
) {
    AppScaffold(
        titleText = "Network List Demo",
        onBackClick = { navigateBack() }
    ) {
        BaseNetWorkListView(
            uiState = uiState,
            onRetry = onRetry,
            customLoading = {
                NetworkListLoadingSkeleton()
            }
        ) {
            NetworkListDemoContent(
                list = list,
                isRefreshing = isRefreshing,
                isLoadingMore = isLoadingMore,
                hasMoreData = hasMoreData,
                onRefresh = onRefresh,
                onLoadMore = onLoadMore
            )
        }
    }
}

/**
 * Network List Demo 内容视图
 *
 * @param list 商品列表数据
 * @param isRefreshing 是否正在刷新
 * @param isLoadingMore 是否正在加载更多
 * @param hasMoreData 是否还有更多数据
 * @param onRefresh 刷新回调
 * @param onLoadMore 加载更多回调
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NetworkListDemoContent(
    list: List<Goods>,
    isRefreshing: Boolean,
    isLoadingMore: Boolean,
    hasMoreData: Boolean,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
) {
    RefreshLayout(
        list = list,
        isRefreshing = isRefreshing,
        isLoadingMore = isLoadingMore,
        hasMoreData = hasMoreData,
        onRefresh = onRefresh,
        onLoadMore = onLoadMore
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
            hasMoreData = true
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
            hasMoreData = true
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
