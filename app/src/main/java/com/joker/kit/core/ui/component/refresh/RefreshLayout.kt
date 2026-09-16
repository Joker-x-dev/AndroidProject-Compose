package com.joker.kit.core.ui.component.refresh

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.joker.kit.R
import com.joker.kit.core.designsystem.theme.AppColors
import com.joker.kit.core.designsystem.theme.SpaceHorizontalMedium
import com.joker.kit.core.designsystem.theme.SpacePaddingMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.ui.component.scaffold.LocalMiuixScrollBehavior
import kotlinx.coroutines.flow.distinctUntilChanged
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState

/**
 * Miuix 刷新布局。
 *
 * 下拉刷新使用 [PullToRefresh]，加载更多使用列表滚动状态接近末尾时的响应式检测。
 *
 * @param modifier 内容修饰符
 * @param list 列表数据
 * @param isGrid 是否使用瀑布网格
 * @param listState 列表滚动状态
 * @param gridState 网格滚动状态
 * @param isRefreshing 下拉刷新状态
 * @param isLoadingMore 加载更多状态
 * @param hasMoreData 是否存在更多数据
 * @param scrollBehavior 顶部栏滚动行为
 * @param contentPadding Scaffold 提供的安全区内边距
 * @param pagePadding 列表页面的统一内容内边距
 * @param onRefresh 下拉刷新回调
 * @param onLoadMore 加载更多回调
 * @param gridContent 网格内容
 * @param content 列表内容
 */
@Composable
fun <T> RefreshLayout(
    modifier: Modifier = Modifier,
    list: List<T>,
    isGrid: Boolean = false,
    listState: LazyListState? = null,
    gridState: LazyStaggeredGridState? = null,
    isRefreshing: Boolean = false,
    isLoadingMore: Boolean = false,
    hasMoreData: Boolean = false,
    scrollBehavior: ScrollBehavior? = null,
    contentPadding: PaddingValues = PaddingValues(),
    pagePadding: PaddingValues = PaddingValues(
        horizontal = SpaceHorizontalMedium,
        vertical = SpaceVerticalMedium,
    ),
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    gridContent: LazyStaggeredGridScope.() -> Unit = {},
    content: LazyListScope.() -> Unit = {},
) {
    // 列表状态默认由刷新布局持有，页面只在需要跨组件控制滚动时显式传入。
    val actualListState = listState ?: rememberLazyListState()
    val actualGridState = gridState ?: rememberLazyStaggeredGridState()
    // 优先复用 AppScaffold 提供的滚动行为，脱离页面骨架时自动创建独立行为。
    val actualScrollBehavior = scrollBehavior
        ?: LocalMiuixScrollBehavior.current
        ?: MiuixScrollBehavior()
    val pullToRefreshState = rememberPullToRefreshState()
    var requestedItemCount by remember { mutableIntStateOf(-1) }

    LaunchedEffect(actualListState, actualGridState, isGrid, list.size, hasMoreData, isLoadingMore) {
        snapshotFlow {
            if (isGrid) {
                val layoutInfo = actualGridState.layoutInfo
                val lastVisible = layoutInfo.visibleItemsInfo.maxOfOrNull { it.index } ?: -1
                lastVisible >= 0 && lastVisible >= layoutInfo.totalItemsCount - 3
            } else {
                val layoutInfo = actualListState.layoutInfo
                val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                lastVisible >= 0 && lastVisible >= layoutInfo.totalItemsCount - 3
            }
        }
            .distinctUntilChanged()
            .collect { nearEnd ->
                if (nearEnd && hasMoreData && !isLoadingMore && requestedItemCount != list.size) {
                    requestedItemCount = list.size
                    onLoadMore()
                }
            }
    }

    PullToRefresh(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        pullToRefreshState = pullToRefreshState,
        // 下拉指示器使用 Scaffold 安全区，列表页面间距由 MiuixPageLazyColumn 处理。
        contentPadding = contentPadding,
        topAppBarScrollBehavior = actualScrollBehavior,
        refreshTexts = listOf(
            stringResource(R.string.refresh_pull_down),
            stringResource(R.string.refresh_release),
            stringResource(R.string.refresh_refreshing),
            stringResource(R.string.refresh_complete),
        ),
    ) {
        RefreshContent(
            isGrid = isGrid,
            listState = actualListState,
            gridState = actualGridState,
            contentPadding = contentPadding,
            pagePadding = pagePadding,
            gridContent = gridContent,
            content = {
                content()
                if (isLoadingMore) {
                    item(key = "refresh-loading-more") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = SpaceVerticalLarge),
                            contentAlignment = Alignment.Center,
                        ) {
                            InfiniteProgressIndicator()
                        }
                    }
                } else if (!hasMoreData && list.isNotEmpty()) {
                    item(key = "refresh-no-more") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = SpaceVerticalLarge),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(R.string.load_more_no_more),
                                color = AppColors.onSurfaceSecondary,
                            )
                        }
                    }
                }
            },
        )
    }
}
