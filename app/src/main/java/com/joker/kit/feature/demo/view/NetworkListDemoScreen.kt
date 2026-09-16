package com.joker.kit.feature.demo.view

import androidx.compose.ui.res.stringResource
import com.joker.kit.R

import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.annotation.ScreenPreview
import com.joker.kit.core.annotation.ScreenPreviewDark
import com.joker.kit.core.base.state.BaseNetWorkListUiState
import com.joker.kit.core.data.preview.GoodsPreviewParameterProvider
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.network.BaseNetWorkListView
import com.joker.kit.core.ui.component.refresh.RefreshLayout
import com.joker.kit.core.ui.component.scaffold.AppScaffold
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
        titleText = stringResource(R.string.network_list_title),
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
        nestedScrollInScaffold = false,
    ) { paddingValues ->
        BaseNetWorkListView(
            uiState = uiState,
            onRetry = onRetry,
            customLoading = {
                NetworkListLoadingSkeleton(contentPadding = paddingValues)
            }
        ) {
            NetworkListDemoContent(
                contentPadding = paddingValues,
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
 * Network List Demo 页面内容
 *
 * @param list 商品列表数据
 * @param isRefreshing 是否正在刷新
 * @param isLoadingMore 是否正在加载更多
 * @param hasMoreData 是否还有更多数据
 * @param onRefresh 刷新回调
 * @param onLoadMore 加载更多回调
 * @author Joker.X
 */
@Composable
private fun NetworkListDemoContent(
    contentPadding: PaddingValues,
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
        contentPadding = contentPadding,
        onRefresh = onRefresh,
        onLoadMore = onLoadMore,
    ) {
        items(list, key = { it.id }) { item ->
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
    Card(modifier = Modifier.fillMaxWidth()) {
        BasicComponent(
            title = goods.title.ifBlank { "未命名商品" },
            summary = goods.subTitle?.ifBlank { "暂无描述" } ?: "暂无描述",
            endActions = { Text(text = stringResource(R.string.goods_price_format, goods.price)) },
        )
    }
}

/**
 * Network List Demo 界面浅色主题预览
 *
 * @param goods 商品预览数据
 * @author Joker.X
 */
@ScreenPreview
@Composable
private fun NetworkListDemoScreenPreview(
    @PreviewParameter(GoodsPreviewParameterProvider::class)
    goods: List<Goods>,
) {
    AppTheme {
        NetworkListDemoScreen(
            // 空列表展示空状态，其他数据展示成功状态
            uiState = if (goods.isEmpty()) {
                BaseNetWorkListUiState.Empty
            } else {
                BaseNetWorkListUiState.Success
            },
            list = goods,
            hasMoreData = goods.isNotEmpty(),
        )
    }
}

/**
 * Network List Demo 界面深色主题预览
 *
 * @param goods 商品预览数据
 * @author Joker.X
 */
@ScreenPreviewDark
@Composable
private fun NetworkListDemoScreenPreviewDark(
    @PreviewParameter(GoodsPreviewParameterProvider::class)
    goods: List<Goods>,
) {
    AppTheme(darkTheme = true) {
        NetworkListDemoScreen(
            uiState = if (goods.isEmpty()) {
                BaseNetWorkListUiState.Empty
            } else {
                BaseNetWorkListUiState.Success
            },
            list = goods,
            hasMoreData = goods.isNotEmpty(),
        )
    }
}
