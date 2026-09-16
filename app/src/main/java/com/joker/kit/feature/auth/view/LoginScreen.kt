package com.joker.kit.feature.auth.view

import androidx.compose.ui.res.stringResource
import com.joker.kit.R

import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Button
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.feature.auth.viewmodel.LoginViewModel
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn

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
@Composable
internal fun LoginScreen(
    onLoginClick: () -> Unit = {},
) {
    AppScaffold(
        titleText = stringResource(R.string.auth_login),
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        LoginContent(
            contentPadding = paddingValues,
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
private fun LoginContent(
    contentPadding: PaddingValues,
    onLoginClick: () -> Unit = {},
) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        pagePadding = PaddingValues(SpacePaddingLarge),
    ) {
        item {
            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.auth_login_action))
            }
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
