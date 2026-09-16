package com.joker.kit.feature.main.component.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.layerBackdrop

/**
 * 主页面 Pager 内容容器。
 *
 * @param pagerState Pager 状态
 * @param userScrollEnabled 是否允许手势滚动
 * @param pageCount 页面数量
 * @param backdrop 页面背景捕获对象
 * @param pageContent 页面内容渲染回调
 */
@Composable
internal fun MainPagerContent(
    pagerState: PagerState,
    userScrollEnabled: Boolean,
    pageCount: Int,
    backdrop: LayerBackdrop,
    pageContent: @Composable (page: Int) -> Unit,
) {
    var contentReady by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        withFrameNanos { }
        contentReady = true
    }
    val settledPage = pagerState.settledPage

    Box(modifier = Modifier.layerBackdrop(backdrop)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = if (contentReady) pageCount - 1 else 0,
            overscrollEffect = null,
            userScrollEnabled = userScrollEnabled,
            verticalAlignment = Alignment.Top,
        ) { page ->
            val isCurrentPage = page == settledPage
            if (contentReady || isCurrentPage) {
                pageContent(page)
            }
        }
    }
}
