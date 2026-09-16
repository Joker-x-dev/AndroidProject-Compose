package com.joker.kit.core.ui.component.appbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.joker.kit.R
import com.joker.kit.core.designsystem.theme.ArrowLeftIcon
import com.joker.kit.core.designsystem.theme.CommonIcon
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton

/**
 * 回退按钮组件
 *
 * @param modifier 修饰符，用于自定义组件样式
 * @param tint 图标颜色，默认使用Unspecified颜色
 * @param onClick 点击回调函数
 * @author Joker.X
 */
@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    onClick: () -> Unit = {}
) {
    IconButton(modifier = modifier, onClick = onClick) {
        ArrowLeftIcon()
    }
}
