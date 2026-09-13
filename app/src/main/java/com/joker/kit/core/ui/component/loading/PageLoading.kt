package com.joker.kit.core.ui.component.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.joker.kit.R
import com.joker.kit.core.designsystem.component.SpaceVerticalSmall
import com.joker.kit.core.designsystem.theme.AppTextStyles
import com.joker.kit.core.designsystem.theme.AppTheme
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Text

/**
 * 页面加载中
 *
 * @param modifier 可选修饰符
 * @author Joker.X
 */
@Composable
fun PageLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        InfiniteProgressIndicator()
        SpaceVerticalSmall()
        Text(text = stringResource(R.string.loading), style = AppTextStyles.body2)
    }
}

/**
 * 页面加载状态预览
 */
@Preview(showBackground = true)
@Composable
fun PageLoadingPreview() {
    AppTheme {
        PageLoading()
    }
}
