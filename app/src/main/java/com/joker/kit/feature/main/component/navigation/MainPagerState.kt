package com.joker.kit.feature.main.component.navigation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlin.math.abs

/** 主页面跨页导航使用的弹簧动画参数。 */
private val PAGER_NAVIGATION_SPRING_SPEC: SpringSpec<Float> = spring(
    stiffness = 322.2f,
    dampingRatio = 32.31f / (2f * kotlin.math.sqrt(322.2f)),
    visibilityThreshold = 0.5f,
)

/**
 * 主页面 Pager 状态。
 *
 * @param pagerState Compose Pager 状态
 * @param coroutineScope 动画协程作用域
 * @param animatePageChanges 是否启用弹簧动画
 */
@Stable
internal class MainPagerState(
    val pagerState: PagerState,
    private val coroutineScope: CoroutineScope,
    private val animatePageChanges: Boolean,
) {
    /** 当前 Pager 导航动画任务。 */
    private var navigationJob: Job? = null

    /** 当前是否存在由导航项点击触发的 Pager 动画。 */
    val isProgrammaticNavigationActive: Boolean
        get() = navigationJob?.isActive == true

    /**
     * 切换到目标页签。
     *
     * @param targetIndex 目标页签索引
     */
    fun animateToPage(targetIndex: Int) {
        // 将外部页签索引限制在 Pager 的有效范围内。
        val target = targetIndex.coerceIn(0, pagerState.pageCount - 1)
        // 手势滚动朝向目标页时保持现有动画状态。
        if (target == pagerState.currentPage) return
        navigationJob?.cancel()
        navigationJob = coroutineScope.launch {
            // 当前协程用于避免旧动画结束时清理新动画任务。
            val currentJob = coroutineContext.job
            try {
                if (animatePageChanges) {
                    springAnimateToPage(target)
                } else {
                    pagerState.scrollToPage(target)
                }
            } finally {
                if (navigationJob == currentJob) navigationJob = null
            }
        }
    }

    /**
     * 使用弹簧曲线将 Pager 滚动到目标页。
     *
     * @param target 目标页索引
     */
    private suspend fun springAnimateToPage(target: Int) {
        if (target !in 0 until pagerState.pageCount) return
        var shouldSnapToTarget = false
        pagerState.scroll(MutatePriority.UserInput) {
            // 页面尺寸包含页面间距，用于将页索引差转换为像素距离。
            val pageSize = pagerState.layoutInfo.pageSize + pagerState.layoutInfo.pageSpacing
            // 当前页偏移量用于从手势中间位置继续执行动画。
            val distance = target - pagerState.currentPage - pagerState.currentPageOffsetFraction
            val scrollPixels = distance * pageSize
            if (abs(scrollPixels) <= 0.5f) return@scroll
            // 累计实际消费的滚动距离，避免动画值与 Pager 消费值脱节。
            var consumedScroll = 0f
            // 滚动受限时停止继续消费动画帧并在结束后吸附目标页。
            var skipScroll = false
            Animatable(0f).animateTo(
                targetValue = scrollPixels,
                animationSpec = PAGER_NAVIGATION_SPRING_SPEC,
            ) {
                if (skipScroll) return@animateTo
                val delta = value - consumedScroll
                if (abs(delta) > 0.5f) {
                    val consumed = scrollBy(delta)
                    consumedScroll += consumed
                    if (abs(delta - consumed) > 0.1f) {
                        shouldSnapToTarget = true
                        skipScroll = true
                    }
                } else {
                    consumedScroll = value
                }
                if (abs(velocity) < 0.1f && abs(scrollPixels - consumedScroll) < 1f) {
                    skipScroll = true
                }
            }
            // 动画结束后的剩余像素通过一次滚动补齐。
            val remaining = scrollPixels - consumedScroll
            if (abs(remaining) > 0.5f) scrollBy(remaining)
        }
        if (shouldSnapToTarget || pagerState.currentPage != target) {
            pagerState.scrollToPage(target)
        }
    }
}

/**
 * 创建主页面 Pager 状态。
 *
 * @param pagerState Compose Pager 状态
 * @param coroutineScope 动画协程作用域
 * @param animatePageChanges 是否启用弹簧动画
 * @return 主页面 Pager 状态
 */
@Composable
internal fun rememberMainPagerState(
    pagerState: PagerState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    animatePageChanges: Boolean = true,
): MainPagerState = remember(pagerState, coroutineScope, animatePageChanges) {
    MainPagerState(pagerState, coroutineScope, animatePageChanges)
}
