package com.joker.kit.core.ui.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.joker.kit.core.annotation.ComponentPreviewLightAndDark
import com.joker.kit.core.designsystem.theme.AppColors
import com.joker.kit.core.designsystem.theme.AppTextStyles
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.SpaceHorizontalLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalSmall
import com.joker.kit.core.state.AppDialogAction
import com.joker.kit.core.state.AppDialogState
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.VerticalDivider

/**
 * 全局对话框宿主。
 *
 * 消费 [AppDialogState.config] 并复用 [AppWindowDialog] 渲染。
 * 由 MainActivity 统一挂载，任意业务层只需调用 [AppDialogState.show] 或 [AppDialogState.confirm]。
 *
 * 关闭时保留上一份配置直到退场动画结束，保证对话框具备完整的退出动画。
 */
@Composable
fun AppDialogHost() {
    val config = AppDialogState.config.collectAsState().value
    // 保留最近一次配置，使关闭动画期间仍能渲染内容。
    var lastConfig by remember { mutableStateOf<AppDialogState.Config?>(null) }
    if (config != null) {
        lastConfig = config
    }
    // 配置与历史配置都为空时无需渲染对话框。
    val activeConfig = config ?: lastConfig ?: return
    val visible = config != null

    // 宽屏双列布局在横屏且配置开启时生效，标题摘要与按钮分列展示。
    val windowSize = LocalWindowInfo.current.containerDpSize
    val twoColumn = activeConfig.wide && windowSize.width > windowSize.height

    AppWindowDialog(
        show = visible,
        title = if (twoColumn) null else activeConfig.title,
        summary = if (twoColumn) null else activeConfig.summary,
        maxWidth = activeConfig.maxWidth,
        largeScreen = activeConfig.largeScreen,
        onDismissRequest = {
            // 点击遮罩或返回键时仅在允许关闭的场景下收起对话框。
            if (activeConfig.dismissOnClickOutside && visible) {
                AppDialogState.hide()
                activeConfig.onDismissRequest?.invoke()
            }
        },
        onDismissFinished = {
            // 退场动画结束后释放历史配置，避免残留内容。
            if (!visible) {
                lastConfig = null
            }
        },
    ) {
        if (twoColumn) {
            WideDialogLayout(config = activeConfig)
        } else {
            AppDialogActionRow(actions = activeConfig.actions)
        }
    }
}

/**
 * 宽屏双列对话框布局。
 *
 * 左侧展示标题与摘要，右侧展示操作按钮。
 *
 * @param config 对话框配置
 */
@Composable
private fun WideDialogLayout(config: AppDialogState.Config) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            config.title?.let { title ->
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    style = AppTextStyles.title3,
                    color = AppColors.onBackground,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }
            config.summary?.let { summary ->
                Spacer(Modifier.height(SpaceVerticalSmall))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = summary,
                    style = AppTextStyles.body1,
                    color = AppColors.onSurfaceSecondary,
                    textAlign = TextAlign.Center,
                )
            }
        }

        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = SpaceHorizontalLarge),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(
                space = SpaceVerticalSmall,
                alignment = Alignment.CenterVertically,
            ),
        ) {
            AppDialogActionRow(
                actions = config.actions,
                horizontal = false,
            )
        }
    }
}

/** 对话框按钮区域浅色与深色预览。 */
@ComponentPreviewLightAndDark
@Composable
private fun AppDialogActionsPreview() {
    AppTheme {
        AppDialogActionRow(
            actions = listOf(
                AppDialogAction(text = "取消", onClick = {}),
                AppDialogAction(text = "确定", onClick = {}, isPrimary = true),
            ),
        )
    }
}

/** 仅确定按钮的浅色与深色预览。 */
@ComponentPreviewLightAndDark
@Composable
private fun AppDialogOnlyConfirmPreview() {
    AppTheme {
        AppDialogActionRow(
            actions = listOf(
                AppDialogAction(text = "知道了", onClick = {}, isPrimary = true),
            ),
        )
    }
}
