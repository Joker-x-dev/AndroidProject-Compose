package com.joker.kit.feature.main.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.ArrowRightIcon
import com.joker.kit.feature.main.model.DemoCardInfo
import top.yukonga.miuix.kmp.basic.BasicComponent

/**
 * Demo 列表项组件，使用 Miuix BasicComponent 展示标题、描述和导航箭头。
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
    BasicComponent(
        modifier = modifier,
        title = info.title,
        summary = info.description,
        endActions = {
            ArrowRightIcon()
        },
        onClick = onClick,
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
