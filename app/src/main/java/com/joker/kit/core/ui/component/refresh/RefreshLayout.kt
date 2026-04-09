package com.joker.kit.core.ui.component.refresh

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.joker.kit.R
import com.joker.kit.core.designsystem.theme.SpaceVerticalLarge
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.core.ui.component.text.TextType
import com.king.ultraswiperefresh.UltraSwipeRefresh
import com.king.ultraswiperefresh.UltraSwipeRefreshState
import com.king.ultraswiperefresh.indicator.classic.ClassicRefreshFooter
import com.king.ultraswiperefresh.indicator.classic.ClassicRefreshHeader
import com.king.ultraswiperefresh.rememberUltraSwipeRefreshState

/**
 * 基于 UltraSwipeRefresh 的刷新布局
 *
 * 统一封装下拉刷新与上拉加载，并兼容列表与网格两种内容展示模式。
 *
 * @param modifier 修饰符
 * @param list 列表数据
 * @param isGrid 是否为网格模式
 * @param listState 列表滚动状态
 * @param gridState 网格滚动状态
 * @param isRefreshing 是否正在刷新
 * @param isLoadingMore 是否正在加载更多
 * @param hasMoreData 是否还有更多数据
 * @param scrollBehavior 顶部导航栏滚动行为
 * @param onRefresh 下拉刷新回调
 * @param onLoadMore 上拉加载回调
 * @param headerIndicator 刷新头部指示器
 * @param noMoreContent 没有更多数据时的底部指示器内容
 * @param gridContent 网格内容构建器
 * @param content 列表内容构建器
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3Api::class)
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
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    headerIndicator: @Composable (UltraSwipeRefreshState) -> Unit = { state ->
        ClassicRefreshHeader(state)
    },
    noMoreContent: @Composable () -> Unit = {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AppText(
                text = stringResource(R.string.load_more_no_more),
                type = TextType.SECONDARY,
                modifier = Modifier.padding(vertical = SpaceVerticalLarge)
            )
        }
    },
    gridContent: LazyStaggeredGridScope.() -> Unit = {},
    content: LazyListScope.() -> Unit = {},
) {
    val actualListState = listState ?: rememberLazyListState()
    val actualGridState = gridState ?: rememberLazyStaggeredGridState()
    val refreshState = rememberUltraSwipeRefreshState(
        isRefreshing = isRefreshing,
        isLoading = isLoadingMore
    )
    var footerHasMoreData by remember { mutableStateOf(hasMoreData) }

    LaunchedEffect(hasMoreData, isLoadingMore, refreshState.isFinishing) {
        footerHasMoreData = when {
            hasMoreData -> true
            isLoadingMore -> true
            refreshState.isFinishing -> true
            else -> false
        }
    }

    val loadMoreEnabled = remember(list) {
        list.isNotEmpty()
    }

    UltraSwipeRefresh(
        state = refreshState,
        onRefresh = onRefresh,
        onLoadMore = onLoadMore,
        vibrationEnabled = true,
        modifier = modifier.then(
            if (scrollBehavior != null) {
                Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            } else {
                Modifier
            }
        ),
        loadMoreEnabled = loadMoreEnabled,
        onCollapseScroll = { offset ->
            if (offset > 0f) {
                if (isGrid) {
                    actualGridState.animateScrollBy(offset)
                } else {
                    actualListState.animateScrollBy(offset)
                }
            }
        },
        headerIndicator = headerIndicator,
        footerIndicator = { state ->
            if (footerHasMoreData) {
                ClassicRefreshFooter(state)
            } else {
                noMoreContent()
            }
        }
    ) {
        RefreshContent(
            isGrid = isGrid,
            listState = actualListState,
            gridState = actualGridState,
            gridContent = gridContent,
            content = content
        )
    }
}
