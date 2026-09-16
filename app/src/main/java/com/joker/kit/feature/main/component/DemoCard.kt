package com.joker.kit.feature.main.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joker.kit.R
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.feature.main.model.DemoCardInfo
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.preference.ArrowPreference

/**
 * Demo 列表项组件，使用 Miuix ArrowPreference 展示标题、描述和导航箭头。
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
    ArrowPreference(
        modifier = modifier,
        title = stringResource(info.titleResId),
        summary = stringResource(info.descriptionResId),
        onClick = onClick,
    )
}

@Preview(showBackground = true)
@Composable
private fun DemoCardPreview() {
    AppTheme {
        Card {
            DemoCard(
                info = DemoCardInfo(
                    titleResId = R.string.demo_card_preview_title,
                    descriptionResId = R.string.demo_card_preview_description,
                    navigateAction = null,
                ),
            )
        }
    }
}
