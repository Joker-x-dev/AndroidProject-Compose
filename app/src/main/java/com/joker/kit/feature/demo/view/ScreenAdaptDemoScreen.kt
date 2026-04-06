package com.joker.kit.feature.demo.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.BgPurpleLight
import com.joker.kit.core.designsystem.theme.ShapeSmall
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpacePaddingMedium
import com.joker.kit.core.designsystem.theme.SpacePaddingSmall
import com.joker.kit.core.designsystem.theme.SpaceVerticalLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalSmall
import com.joker.kit.core.designsystem.theme.TextSubtitleLight
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.adaptive.bp
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.core.ui.component.text.TextSize
import com.joker.kit.feature.demo.viewmodel.ScreenAdaptDemoViewModel
import kotlin.math.ceil

/**
 * 屏幕适配示例页面路由
 *
 * @param viewModel 屏幕适配示例页面 ViewModel
 * @author Joker.X
 */
@Composable
internal fun ScreenAdaptDemoRoute(
    viewModel: ScreenAdaptDemoViewModel = hiltViewModel()
) {
    ScreenAdaptDemoScreen()
}

/**
 * 屏幕适配示例页面
 *
 * @author Joker.X
 */
@Composable
internal fun ScreenAdaptDemoScreen() {
    AppScaffold(
        titleText = "屏幕适配示例",
        onBackClick = { navigateBack() }
    ) {
        ScreenAdaptDemoContentView()
    }
}

/**
 * 屏幕适配示例页面内容视图
 *
 * @author Joker.X
 */
@Composable
private fun ScreenAdaptDemoContentView() {
    val breakpointCode = bp(xs = "xs", sm = "sm", md = "md", lg = "lg")
    val gridColumns = bp(xs = 2, sm = 2, md = 3, lg = 4)
    val listColumns = bp(xs = 1, sm = 1, md = 2, lg = 2)
    val sampleTextSize = bp(
        xs = TextSize.BODY_LARGE,
        sm = TextSize.BODY_LARGE,
        md = TextSize.TITLE_LARGE,
        lg = TextSize.DISPLAY_LARGE
    )
    val sampleBoxSize = bp(xs = 80.dp, sm = 80.dp, md = 96.dp, lg = 120.dp)
    val gridItems = listOf(1, 2, 3, 4, 5, 6)
    val listItems = listOf(1, 2, 3, 4)

    LazyColumn(
        contentPadding = PaddingValues(SpacePaddingLarge),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalLarge)
    ) {
        item {
            AdaptSectionTitle(title = "当前断点")
        }
        item {
            BreakpointCard(
                breakpointCode = breakpointCode
            )
        }
        item {
            AdaptSectionTitle(title = "网格布局示例")
        }
        item {
            AdaptiveGridSection(
                columns = gridColumns,
                items = gridItems
            )
        }
        item {
            AdaptSectionTitle(title = "列表布局示例")
        }
        item {
            AdaptiveListSection(
                columns = listColumns,
                items = listItems
            )
        }
        item {
            AdaptSectionTitle(title = "文本/大小适配")
        }
        item {
            TextAdaptSection(sampleTextSize = sampleTextSize)
        }
        item {
            AdaptSectionTitle(title = "大小适配")
        }
        item {
            SizeAdaptSection(sampleBoxSize = sampleBoxSize)
        }
    }
}

/**
 * 区块标题
 *
 * @param title 标题
 * @author Joker.X
 */
@Composable
private fun AdaptSectionTitle(
    title: String
) {
    AppText(
        text = title,
        size = TextSize.TITLE_LARGE
    )
}

/**
 * 断点信息卡片
 *
 * @param breakpointCode 当前断点编码
 * @author Joker.X
 */
@Composable
private fun BreakpointCard(
    breakpointCode: String
) {
    AdaptiveCard(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardTitle(
            title = "当前断点",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        CardDesc(
            text = breakpointCode,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 网格布局示例
 *
 * @param columns 列数
 * @param items 数据项
 * @author Joker.X
 */
@Composable
private fun AdaptiveGridSection(
    columns: Int,
    items: List<Int>
) {
    MeasuredGrid(
        columns = columns,
        items = items
    ) { item, modifier ->
        AdaptiveCard(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardTitle(
                title = "网格项 $item",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(SpaceVerticalSmall))
            CardDesc(
                text = "根据屏幕大小调整列数",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 列表布局示例
 *
 * @param columns 列数
 * @param items 数据项
 * @author Joker.X
 */
@Composable
private fun AdaptiveListSection(
    columns: Int,
    items: List<Int>
) {
    MeasuredGrid(
        columns = columns,
        items = items
    ) { item, modifier ->
        AdaptiveCard(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardTitle(
                title = "列表项 $item",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(SpaceVerticalSmall))
            CardDesc(
                text = "小屏一列，大屏两列",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 文本适配示例
 *
 * @param sampleTextSize 示例文字大小
 * @author Joker.X
 */
@Composable
private fun TextAdaptSection(
    sampleTextSize: TextSize
) {
    AdaptiveCard(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CardDesc(
                text = "适配文本",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(SpacePaddingSmall))
            AppText(
                text = "示例文字会随屏幕变化",
                size = sampleTextSize,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 尺寸适配示例
 *
 * @param sampleBoxSize 示例尺寸
 * @author Joker.X
 */
@Composable
private fun SizeAdaptSection(
    sampleBoxSize: Dp
) {
    AdaptiveCard {
        CardTitle(
            title = "尺寸随屏幕变化",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        CardDesc(
            text = "通过 bp 设置不同断点尺寸",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SpacePaddingSmall)
        ) {
            CardDesc(
                text = "示例尺寸：${sampleBoxSize.value.toInt()}dp",
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(sampleBoxSize)
                    .background(color = BgPurpleLight, shape = ShapeSmall)
            )
        }
    }
}

/**
 * 按实际子项高度测量网格
 *
 * @param columns 列数
 * @param items 数据项
 * @param itemContent 子项内容
 * @author Joker.X
 */
@Composable
private fun <T> MeasuredGrid(
    columns: Int,
    items: List<T>,
    itemContent: @Composable (item: T, modifier: Modifier) -> Unit
) {
    val density = LocalDensity.current
    val safeColumns = columns.coerceAtLeast(1)
    val rowCount = ceil(items.size / safeColumns.toFloat()).toInt().coerceAtLeast(1)
    var measuredItemHeightPx by remember(columns, items.size) { mutableIntStateOf(0) }
    val fallbackItemHeight = 84.dp
    val itemHeight = with(density) {
        if (measuredItemHeightPx > 0) measuredItemHeightPx.toDp() else fallbackItemHeight
    }
    val gridHeight = (itemHeight * rowCount) + (SpacePaddingSmall * (rowCount - 1))

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .height(gridHeight),
        columns = GridCells.Fixed(safeColumns),
        horizontalArrangement = Arrangement.spacedBy(SpacePaddingSmall),
        verticalArrangement = Arrangement.spacedBy(SpacePaddingSmall),
        userScrollEnabled = false
    ) {
        items(items) { item ->
            itemContent(
                item,
                Modifier.onSizeChanged { size ->
                    if (size.height > measuredItemHeightPx) {
                        measuredItemHeightPx = size.height
                    }
                }
            )
        }
    }
}

/**
 * 通用适配卡片
 *
 * @param content 卡片内容
 * @author Joker.X
 */
@Composable
private fun AdaptiveCard(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(SpacePaddingMedium),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }
}

/**
 * 卡片标题
 *
 * @param title 标题
 * @author Joker.X
 */
@Composable
private fun CardTitle(
    title: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null
) {
    AppText(
        text = title,
        size = TextSize.BODY_LARGE,
        modifier = modifier,
        textAlign = textAlign
    )
}

/**
 * 卡片说明
 *
 * @param text 文本
 * @param modifier 修饰符
 * @author Joker.X
 */
@Composable
private fun CardDesc(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null
) {
    AppText(
        text = text,
        size = TextSize.BODY_MEDIUM,
        color = TextSubtitleLight,
        modifier = modifier,
        textAlign = textAlign
    )
}

/**
 * 屏幕适配示例页面浅色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun ScreenAdaptDemoPreview() {
    AppTheme {
        ScreenAdaptDemoScreen()
    }
}

/**
 * 屏幕适配示例页面深色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun ScreenAdaptDemoPreviewDark() {
    AppTheme(darkTheme = true) {
        ScreenAdaptDemoScreen()
    }
}
