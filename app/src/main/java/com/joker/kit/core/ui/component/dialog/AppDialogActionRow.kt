package com.joker.kit.core.ui.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joker.kit.core.state.AppDialogAction
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextButtonColors

/**
 * 对话框操作按钮区域。
 *
 * 按钮数量不超过两个时横向等宽排列，三个及以上时纵向排列，
 * 与 Miuix 官方 Dialog 示例的按钮排布保持一致。
 *
 * @param actions 按钮配置列表
 * @param modifier 修饰符
 * @param horizontal 是否强制横向排列，为空时按按钮数量自动选择
 */
@Composable
fun AppDialogActionRow(
    actions: List<AppDialogAction>,
    modifier: Modifier = Modifier,
    horizontal: Boolean? = null,
) {
    if (actions.isEmpty()) return

    val useHorizontal = horizontal ?: (actions.size <= 2)
    val colorsOf: @Composable (AppDialogAction) -> TextButtonColors = { action ->
        val overrideColor = action.color
        when {
            action.isPrimary && overrideColor != null ->
                ButtonDefaults.textButtonColorsPrimary(color = overrideColor)

            action.isPrimary -> ButtonDefaults.textButtonColorsPrimary()
            overrideColor != null -> ButtonDefaults.textButtonColors(color = overrideColor)
            else -> ButtonDefaults.textButtonColors()
        }
    }

    if (useHorizontal) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            actions.forEach { action ->
                TextButton(
                    text = action.text,
                    onClick = action.onClick,
                    modifier = Modifier.weight(1f),
                    colors = colorsOf(action),
                )
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            actions.forEach { action ->
                TextButton(
                    text = action.text,
                    onClick = action.onClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = colorsOf(action),
                )
            }
        }
    }
}
