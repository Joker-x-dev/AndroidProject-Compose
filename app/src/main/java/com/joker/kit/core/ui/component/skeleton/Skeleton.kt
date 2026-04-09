package com.joker.kit.core.ui.component.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.ShapeMedium
import com.joker.kit.core.designsystem.theme.ShapeSmall
import com.joker.kit.core.designsystem.theme.SpaceHorizontalMedium
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalSmall
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

/**
 * 默认头像尺寸
 */
private val DefaultSkeletonAvatarSize = 40.dp

/**
 * 默认图片尺寸
 */
private val DefaultSkeletonImageSize = 96.dp

/**
 * 默认文本高度
 */
private val DefaultSkeletonTextLineHeight = 16.dp

/**
 * 默认文本间距
 */
private val DefaultSkeletonLineSpacing = SpaceVerticalSmall

/**
 * 默认段落宽度比例
 */
private val DefaultSkeletonLineFractions = listOf(0.42f, 1f, 1f, 0.64f)

/**
 * 骨架屏默认底色
 *
 * @return 骨架屏底色
 * @author Joker.X
 */
@Composable
private fun defaultSkeletonColor(): Color {
    val colorScheme = MaterialTheme.colorScheme
    val isDarkBackground = colorScheme.background.luminance() < 0.5f
    return if (isDarkBackground) {
        colorScheme.onSurface.copy(alpha = 0.14f)
    } else {
        colorScheme.onSurface.copy(alpha = 0.1f)
    }
}

/**
 * 骨架屏修饰符
 *
 * 统一处理圆角、底色和 shimmer 动效，便于应用在任意占位容器上。
 *
 * @param enabled 是否启用骨架屏
 * @param shape 骨架形状
 * @param color 骨架底色
 * @param shimmerEnabled 是否启用 shimmer
 * @author Joker.X
 */
fun Modifier.skeleton(
    enabled: Boolean = true,
    shape: Shape = ShapeMedium,
    color: Color? = null,
    shimmerEnabled: Boolean = true,
): Modifier = composed {
    if (!enabled) {
        return@composed this
    }
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    val containerColor = color ?: defaultSkeletonColor()
    var modifier = this.clip(shape)
    if (shimmerEnabled) {
        modifier = modifier.shimmer(shimmerInstance)
    }
    modifier.background(containerColor)
}

/**
 * 骨架屏内容切换容器
 *
 * @param loading 是否处于加载态
 * @param skeleton 骨架屏内容
 * @param content 实际内容
 * @author Joker.X
 */
@Composable
fun SkeletonContent(
    loading: Boolean,
    skeleton: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    if (loading) {
        skeleton()
    } else {
        content()
    }
}

/**
 * 骨架矩形块
 *
 * @param modifier 修饰符
 * @param shape 形状
 * @param color 颜色
 * @param shimmerEnabled 是否启用 shimmer
 * @author Joker.X
 */
@Composable
fun SkeletonBlock(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = defaultSkeletonColor(),
    shimmerEnabled: Boolean = true,
) {
    Spacer(
        modifier = modifier.skeleton(
            shape = shape,
            color = color,
            shimmerEnabled = shimmerEnabled,
        )
    )
}

/**
 * 圆形骨架块
 *
 * @param modifier 修饰符
 * @param size 尺寸
 * @param color 颜色
 * @param shimmerEnabled 是否启用 shimmer
 * @author Joker.X
 */
@Composable
fun SkeletonCircle(
    modifier: Modifier = Modifier,
    size: Dp = DefaultSkeletonAvatarSize,
    color: Color = defaultSkeletonColor(),
    shimmerEnabled: Boolean = true,
) {
    SkeletonBlock(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = color,
        shimmerEnabled = shimmerEnabled,
    )
}

/**
 * 文本骨架行
 *
 * @param modifier 修饰符
 * @param widthFraction 宽度比例
 * @param height 行高
 * @param shape 形状
 * @param color 颜色
 * @param shimmerEnabled 是否启用 shimmer
 * @author Joker.X
 */
@Composable
fun SkeletonTextLine(
    modifier: Modifier = Modifier,
    widthFraction: Float = 1f,
    height: Dp = DefaultSkeletonTextLineHeight,
    shape: Shape = ShapeSmall,
    color: Color = defaultSkeletonColor(),
    shimmerEnabled: Boolean = true,
) {
    SkeletonBlock(
        modifier = modifier
            .fillMaxWidth(widthFraction.coerceIn(0f, 1f))
            .height(height),
        shape = shape,
        color = color,
        shimmerEnabled = shimmerEnabled,
    )
}

/**
 * 图片骨架块
 *
 * @param modifier 修饰符
 * @param size 尺寸
 * @param shape 形状
 * @param color 颜色
 * @param shimmerEnabled 是否启用 shimmer
 * @author Joker.X
 */
@Composable
fun SkeletonImage(
    modifier: Modifier = Modifier,
    size: Dp = DefaultSkeletonImageSize,
    shape: Shape = ShapeMedium,
    color: Color = defaultSkeletonColor(),
    shimmerEnabled: Boolean = true,
) {
    SkeletonBlock(
        modifier = modifier.size(size),
        shape = shape,
        color = color,
        shimmerEnabled = shimmerEnabled,
    )
}

/**
 * 段落骨架屏
 *
 * 支持头像、文本行数和每行宽度比例配置，适合快速搭建列表或详情说明类骨架。
 *
 * @param modifier 修饰符
 * @param rows 行数
 * @param lineFractions 每行宽度比例
 * @param lineHeight 文本高度
 * @param lineSpacing 文本间距
 * @param avatar 是否显示头像骨架
 * @param avatarSize 头像尺寸
 * @param color 颜色
 * @param shimmerEnabled 是否启用 shimmer
 * @author Joker.X
 */
@Composable
fun SkeletonParagraph(
    modifier: Modifier = Modifier,
    rows: Int = 4,
    lineFractions: List<Float> = DefaultSkeletonLineFractions,
    lineHeight: Dp = DefaultSkeletonTextLineHeight,
    lineSpacing: Dp = DefaultSkeletonLineSpacing,
    avatar: Boolean = false,
    avatarSize: Dp = DefaultSkeletonAvatarSize,
    color: Color = defaultSkeletonColor(),
    shimmerEnabled: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(SpaceHorizontalMedium),
    ) {
        if (avatar) {
            SkeletonCircle(
                size = avatarSize,
                color = color,
                shimmerEnabled = shimmerEnabled,
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(lineSpacing),
        ) {
            val widths = remember(rows, lineFractions) {
                if (lineFractions.isNotEmpty()) {
                    List(rows) { index ->
                        lineFractions.getOrElse(index) { lineFractions.last() }
                    }
                } else {
                    List(rows) { 1f }
                }
            }
            widths.forEach { widthFraction ->
                SkeletonTextLine(
                    widthFraction = widthFraction,
                    height = lineHeight,
                    color = color,
                    shimmerEnabled = shimmerEnabled,
                )
            }
        }
    }
}

/**
 * 卡片式骨架模板
 *
 * 适合商品卡片、资料卡片等常见场景，业务可在此基础上继续组合。
 *
 * @param modifier 修饰符
 * @param imageHeight 图片高度
 * @param shimmerEnabled 是否启用 shimmer
 * @author Joker.X
 */
@Composable
fun SkeletonCard(
    modifier: Modifier = Modifier,
    imageHeight: Dp = 160.dp,
    shimmerEnabled: Boolean = true,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium),
    ) {
        SkeletonBlock(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight),
            shape = ShapeMedium,
            shimmerEnabled = shimmerEnabled,
        )
        SkeletonTextLine(
            widthFraction = 0.72f,
            shimmerEnabled = shimmerEnabled,
        )
        SkeletonTextLine(
            widthFraction = 1f,
            shimmerEnabled = shimmerEnabled,
        )
        SkeletonTextLine(
            widthFraction = 0.48f,
            shimmerEnabled = shimmerEnabled,
        )
    }
}

/**
 * 列表示例骨架
 *
 * @param modifier 修饰符
 * @param count 条目数量
 * @param shimmerEnabled 是否启用 shimmer
 * @author Joker.X
 */
@Composable
fun SkeletonList(
    modifier: Modifier = Modifier,
    count: Int = 3,
    shimmerEnabled: Boolean = true,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium),
    ) {
        repeat(count) {
            SkeletonParagraph(
                rows = 3,
                avatar = true,
                lineFractions = listOf(0.48f, 1f, 0.68f),
                shimmerEnabled = shimmerEnabled,
            )
        }
    }
}

/**
 * 骨架屏预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun SkeletonPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacePaddingLarge)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)) {
                SkeletonParagraph(
                    rows = 4,
                    avatar = true,
                )
                SkeletonCard()
                SkeletonList(count = 2)
            }
        }
    }
}
