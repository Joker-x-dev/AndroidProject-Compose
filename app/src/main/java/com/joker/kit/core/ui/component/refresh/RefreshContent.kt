package com.joker.kit.core.ui.component.refresh

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.joker.kit.core.designsystem.theme.SpaceHorizontalMedium
import com.joker.kit.core.designsystem.theme.SpacePaddingMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium

/**
 * 刷新内容区域组件
 *
 * 根据布局模式切换展示列表或网格内容，仅负责内容区域的排版与切换动画。
 *
 * @param isGrid 是否为网格模式
 * @param modifier 修饰符
 * @param listState 列表滚动状态
 * @param gridState 网格滚动状态
 * @param listContentPadding 列表内容内边距
 * @param listVerticalArrangement 列表项纵向排列方式
 * @param gridColumns 网格列数配置
 * @param gridContentPadding 网格内容内边距
 * @param gridHorizontalArrangement 网格横向排列方式
 * @param gridVerticalItemSpacing 网格纵向间距
 * @param gridContent 网格内容构建器
 * @param content 列表内容构建器
 * @author Joker.X
 */
@Composable
fun RefreshContent(
    isGrid: Boolean = false,
    modifier: Modifier = Modifier,
    listState: LazyListState? = null,
    gridState: LazyStaggeredGridState? = null,
    listContentPadding: PaddingValues = PaddingValues(
        horizontal = SpaceHorizontalMedium,
        vertical = SpaceVerticalMedium
    ),
    listVerticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(SpaceVerticalMedium),
    gridColumns: StaggeredGridCells = StaggeredGridCells.Fixed(2),
    gridContentPadding: PaddingValues = PaddingValues(SpacePaddingMedium),
    gridHorizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(SpacePaddingMedium),
    gridVerticalItemSpacing: androidx.compose.ui.unit.Dp = SpacePaddingMedium,
    gridContent: LazyStaggeredGridScope.() -> Unit = {},
    content: LazyListScope.() -> Unit = {},
) {
    AnimatedContent(
        targetState = isGrid,
        transitionSpec = {
            (fadeIn(animationSpec = tween(300, easing = LinearEasing)) +
                scaleIn(
                    initialScale = 0.92f,
                    animationSpec = tween(300, easing = LinearEasing)
                ))
                .togetherWith(
                    fadeOut(animationSpec = tween(300, easing = LinearEasing)) +
                        scaleOut(
                            targetScale = 0.92f,
                            animationSpec = tween(300, easing = LinearEasing)
                        )
                )
        },
        label = "layout_switch_animation"
    ) { targetIsGrid ->
        if (targetIsGrid) {
            RefreshGridContent(
                modifier = modifier,
                gridState = gridState,
                columns = gridColumns,
                contentPadding = gridContentPadding,
                horizontalArrangement = gridHorizontalArrangement,
                verticalItemSpacing = gridVerticalItemSpacing,
                content = gridContent
            )
        } else {
            RefreshListContent(
                modifier = modifier,
                listState = listState,
                contentPadding = listContentPadding,
                verticalArrangement = listVerticalArrangement,
                content = content
            )
        }
    }
}

/**
 * 列表内容组件
 *
 * @param modifier 修饰符
 * @param listState 列表滚动状态
 * @param contentPadding 内容内边距
 * @param verticalArrangement 列表项纵向排列方式
 * @param content 列表内容构建器
 * @author Joker.X
 */
@Composable
private fun RefreshListContent(
    modifier: Modifier = Modifier,
    listState: LazyListState? = null,
    contentPadding: PaddingValues,
    verticalArrangement: Arrangement.Vertical,
    content: LazyListScope.() -> Unit,
) {
    val actualListState = listState ?: rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = actualListState,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        content = content
    )
}

/**
 * 网格内容组件
 *
 * @param modifier 修饰符
 * @param gridState 网格滚动状态
 * @param columns 网格列数配置
 * @param contentPadding 内容内边距
 * @param horizontalArrangement 网格横向排列方式
 * @param verticalItemSpacing 网格纵向间距
 * @param content 网格内容构建器
 * @author Joker.X
 */
@Composable
private fun RefreshGridContent(
    modifier: Modifier = Modifier,
    gridState: LazyStaggeredGridState? = null,
    columns: StaggeredGridCells,
    contentPadding: PaddingValues,
    horizontalArrangement: Arrangement.Horizontal,
    verticalItemSpacing: androidx.compose.ui.unit.Dp,
    content: LazyStaggeredGridScope.() -> Unit,
) {
    val actualGridState = gridState ?: rememberLazyStaggeredGridState()

    LazyVerticalStaggeredGrid(
        columns = columns,
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalItemSpacing = verticalItemSpacing,
        state = actualGridState,
        content = content
    )
}
