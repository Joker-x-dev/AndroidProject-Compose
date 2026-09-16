package com.joker.kit.feature.demo.skeleton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.RadiusMedium
import com.joker.kit.core.designsystem.theme.ShapeMedium
import com.joker.kit.core.designsystem.theme.SpaceHorizontalMedium
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.ui.component.skeleton.SkeletonTextLine
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.core.designsystem.component.SpaceHorizontalLarge
import top.yukonga.miuix.kmp.basic.Card

/**
 * Network List Demo 列表项骨架屏
 *
 * 按列表卡片的排版结构展示标题、描述和价格占位。
 *
 * @author Joker.X
 */
@Composable
private fun NetworkListItemSkeleton() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = RadiusMedium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacePaddingLarge, vertical = SpacePaddingLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
            ) {
                SkeletonTextLine(
                    widthFraction = 0.58f,
                    shape = ShapeMedium
                )
                SkeletonTextLine(
                    widthFraction = 0.82f
                )
            }

            SpaceHorizontalLarge()

            SkeletonTextLine(
                modifier = Modifier.width(72.dp),
                widthFraction = 1f,
                shape = ShapeMedium
            )
        }
    }
}

/**
 * Network List Demo 加载骨架屏
 *
 * 按列表页的卡片间距与上下留白展示骨架占位内容。
 *
 * @param contentPadding 页面脚手架提供的安全区边距
 * @author Joker.X
 */
@Composable
internal fun NetworkListLoadingSkeleton(
    contentPadding: PaddingValues = PaddingValues(),
) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        pagePadding = PaddingValues(
            horizontal = SpaceHorizontalMedium,
            vertical = SpaceVerticalMedium,
        ),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
    ) {
        items((0 until 10).toList()) {
            NetworkListItemSkeleton()
        }
    }
}

/**
 * Network List Demo 加载骨架屏浅色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NetworkListLoadingSkeletonPreview() {
    AppTheme {
        NetworkListLoadingSkeleton()
    }
}

/**
 * Network List Demo 加载骨架屏深色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NetworkListLoadingSkeletonPreviewDark() {
    AppTheme(darkTheme = true) {
        NetworkListLoadingSkeleton()
    }
}
