package com.joker.kit.feature.main.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.ShapeMedium
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.core.ui.component.text.TextSize
import com.joker.kit.feature.main.model.DemoCardInfo

/**
 * Demo 卡片组件，统一 ListItem 样式
 *
 * @param info 卡片数据
 * @param modifier 修饰符
 * @param onClick 点击回调
 * @author Joker.X
 */
@Composable
fun DemoCard(
    info: DemoCardInfo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    ListItem(
        modifier = modifier
            .clip(ShapeMedium)
            .clickable(onClick = onClick),
        headlineContent = {
            AppText(
                text = info.title,
                size = TextSize.TITLE_MEDIUM
            )
        },
        supportingContent = {
            AppText(
                text = info.description,
                size = TextSize.BODY_MEDIUM
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DemoCardPreview() {
    AppTheme {
        DemoCard(
            info = DemoCardInfo(
                title = "示例组件",
                description = "预览展示 Demo 卡片默认样式。",
                navigateAction = null
            )
        )
    }
}
