package com.joker.kit.feature.auth.view

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
import com.joker.kit.feature.auth.viewmodel.LoginViewModel

/**
 * 登录页面路由入口
 *
 * @param viewModel 登录页 ViewModel
 * @author Joker.X
 */
@Composable
internal fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel()
) {
    LoginScreen(
        onLoginClick = viewModel::login
    )
}

/**
 * 登录页面
 *
 * @param onLoginClick 登录按钮回调
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(
    onLoginClick: () -> Unit = {},
) {
    AppScaffold(
        titleText = "登录",
        onBackClick = { navigateBack() },
    ) {
        LoginContentView(
            onLoginClick = onLoginClick
        )
    }
}

/**
 * 登录页面内容
 *
 * @param onLoginClick 登录按钮回调
 * @author Joker.X
 */
@Composable
private fun LoginContentView(
    onLoginClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(SpacePaddingLarge),
    ) {
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppText(text = "一键登录")
        }
    }
}

/**
 * 登录页浅色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreen()
    }
}

/**
 * 登录页深色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun LoginScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        LoginScreen()
    }
}
