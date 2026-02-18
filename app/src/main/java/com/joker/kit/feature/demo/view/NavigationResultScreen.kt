package com.joker.kit.feature.demo.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.feature.demo.viewmodel.NavigationResultViewModel

/**
 * 结果回传示例路由
 *
 * @param viewModel Hilt 注入的 NavigationResultViewModel
 * @author Joker.X
 */
@Composable
internal fun NavigationResultRoute(
    viewModel: NavigationResultViewModel = hiltViewModel()
) {
    NavigationResultScreen(
        onSendResult = viewModel::sendResultAndBack
    )
}

/**
 * 结果回传示例界面
 *
 * @param onSendResult 发送结果并返回回调
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NavigationResultScreen(
    onSendResult: () -> Unit = {},
) {
    AppScaffold(
        titleText = "结果回传",
        onBackClick = { navigateBack() }
    ) {
        NavigationResultContent(onSendResult = onSendResult)
    }
}

/**
 * 结果回传内容视图
 *
 * @param onSendResult 发送结果回调
 * @author Joker.X
 */
@Composable
private fun NavigationResultContent(onSendResult: () -> Unit) {
    Column(modifier = Modifier.padding(SpacePaddingLarge)) {
        Button(
            onClick = onSendResult,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppText(text = "回传结果并返回")
        }
    }
}

/**
 * 结果回传界面浅色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NavigationResultPreview() {
    AppTheme {
        NavigationResultScreen()
    }
}

/**
 * 结果回传界面深色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NavigationResultPreviewDark() {
    AppTheme(darkTheme = true) {
        NavigationResultScreen()
    }
}
