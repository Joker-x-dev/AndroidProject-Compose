package com.joker.kit.core.ui.component.skeleton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.ShapeMedium
import com.joker.kit.core.designsystem.theme.SpaceHorizontalMedium
import com.joker.kit.core.designsystem.theme.SpacePaddingMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalSmall
import com.joker.kit.core.ui.component.title.TitleWithLine

/**
 * Skeleton组件展示页面
 *
 * 用于展示基础骨架块、段落骨架、卡片骨架与列表骨架的组合效果。
 *
 * @author Joker.X
 */
@Composable
fun SkeletonShowcase() {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(SpacePaddingMedium)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium),
        ) {
            Text(
                text = "骨架屏 Skeleton",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            TitleWithLine(text = "基础块")

            Row(
                horizontalArrangement = Arrangement.spacedBy(SpaceHorizontalMedium),
                modifier = Modifier.fillMaxWidth(),
            ) {
                SkeletonCircle(size = 48.dp)
                SkeletonBlock(
                    modifier = Modifier
                        .size(64.dp),
                )
                SkeletonBlock(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = ShapeMedium,
                )
            }

            TitleWithLine(text = "文本段落")

            SkeletonParagraph(
                rows = 4,
                avatar = true,
                lineFractions = listOf(0.36f, 1f, 0.92f, 0.58f),
            )

            SkeletonParagraph(
                rows = 3,
                lineFractions = listOf(0.28f, 1f, 0.44f),
            )

            TitleWithLine(text = "商品卡片")

            SkeletonCard()

            TitleWithLine(text = "列表场景")

            SkeletonList(count = 3)

            TitleWithLine(text = "自定义组合")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SpaceHorizontalMedium),
            ) {
                SkeletonImage(
                    modifier = Modifier.size(88.dp),
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(SpaceVerticalSmall),
                ) {
                    SkeletonTextLine(widthFraction = 0.62f)
                    SkeletonTextLine(widthFraction = 1f)
                    SkeletonTextLine(widthFraction = 0.88f)
                    SkeletonTextLine(widthFraction = 0.36f)
                }
            }
        }
    }
}

/**
 * Skeleton组件展示页面预览 - 浅色主题
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun SkeletonShowcasePreviewLight() {
    AppTheme(darkTheme = false) {
        SkeletonShowcase()
    }
}

/**
 * Skeleton组件展示页面预览 - 深色主题
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun SkeletonShowcasePreviewDark() {
    AppTheme(darkTheme = true) {
        SkeletonShowcase()
    }
}
