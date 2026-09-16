package com.joker.kit.feature.demo.view

import androidx.compose.ui.res.stringResource
import com.joker.kit.R

import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.feature.demo.viewmodel.NavigationResultViewModel
import top.yukonga.miuix.kmp.basic.BasicComponent

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
@Composable
internal fun NavigationResultScreen(
    onSendResult: () -> Unit = {},
) {
    AppScaffold(
        titleText = stringResource(R.string.navigation_result_title),
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        NavigationResultContent(contentPadding = paddingValues, onSendResult = onSendResult)
    }
}

/**
 * 结果回传页面内容
 *
 * @param onSendResult 发送结果回调
 * @author Joker.X
 */
@Composable
private fun NavigationResultContent(contentPadding: PaddingValues, onSendResult: () -> Unit) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        pagePadding = PaddingValues(SpacePaddingLarge),
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                BasicComponent(
                    title = stringResource(R.string.navigation_result_title),
                    summary = stringResource(R.string.navigation_result_operation_summary),
                    bottomAction = {
                        Button(
                            onClick = onSendResult,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = SpaceVerticalMedium),
                        ) {
                            Text(text = stringResource(R.string.navigation_result_action))
                        }
                    },
                )
            }
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
