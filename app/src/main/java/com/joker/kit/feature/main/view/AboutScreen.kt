package com.joker.kit.feature.main.view

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.feature.main.viewmodel.AboutViewModel

/**
 * About 页面路由
 *
 * @param viewModel About 页面 ViewModel
 * @author Joker.X
 */
@Suppress("UNUSED_PARAMETER")
@Composable
internal fun AboutRoute(
    viewModel: AboutViewModel = hiltViewModel()
) {
    AboutScreen()
}

/**
 * About 页面
 *
 * @author Joker.X
 */
@Composable
internal fun AboutScreen() {
    AboutContentView(
        modifier = Modifier.padding(SpacePaddingLarge)
    )
}

/**
 * About 页面内容视图
 *
 * @param modifier 修饰符
 * @author Joker.X
 */
@Composable
private fun AboutContentView(
    modifier: Modifier = Modifier
) {
    AppText(
        text = "关于页面内容占位",
        modifier = modifier
    )
}

/**
 * About 页面浅色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun AboutPreview() {
    AppTheme {
        AboutScreen()
    }
}

/**
 * About 页面深色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun AboutPreviewDark() {
    AppTheme(darkTheme = true) {
        AboutScreen()
    }
}
