package com.joker.kit.feature.main.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Main 子页面统一内容边距。
 *
 * @param scaffoldPadding 页面 Scaffold 提供的系统栏和顶部栏边距
 * @param bottomInnerPadding 主页面底部导航栏占用高度
 * @return 叠加页面内容边距后的 PaddingValues
 */
@Composable
internal fun mainPageContentPadding(
    scaffoldPadding: PaddingValues,
    bottomInnerPadding: Dp = 0.dp,
): PaddingValues {
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    return PaddingValues(
    top = scaffoldPadding.calculateTopPadding() + SpacePaddingLarge,
    // 底部安全区取 Scaffold 与系统导航栏中的较大值，避免内容被底部操作栏遮挡。
    bottom = maxOf(
        scaffoldPadding.calculateBottomPadding(),
        navigationBarPadding,
        bottomInnerPadding,
    ) + SpacePaddingLarge,
    start = SpacePaddingLarge,
    end = SpacePaddingLarge,
    )
}
