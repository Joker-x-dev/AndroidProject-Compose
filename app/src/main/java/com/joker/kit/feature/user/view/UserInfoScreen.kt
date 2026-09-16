package com.joker.kit.feature.user.view

import androidx.compose.ui.res.stringResource
import com.joker.kit.R

import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Button
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.feature.user.viewmodel.UserInfoViewModel
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn

/**
 * 用户信息页面路由入口
 *
 * @param viewModel 用户信息页 ViewModel
 * @author Joker.X
 */
@Composable
internal fun UserInfoRoute(
    viewModel: UserInfoViewModel = hiltViewModel()
) {
    UserInfoScreen(
        onLogoutClick = viewModel::logout
    )
}

/**
 * 用户信息页面
 *
 * @param onLogoutClick 退出登录回调
 * @author Joker.X
 */
@Composable
internal fun UserInfoScreen(
    onLogoutClick: () -> Unit = {},
) {
    AppScaffold(
        titleText = stringResource(R.string.user_info_title),
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        UserInfoContent(
            contentPadding = paddingValues,
            onLogoutClick = onLogoutClick
        )
    }
}

/**
 * 用户信息内容
 *
 * @param modifier 修饰符
 * @param onLogoutClick 退出登录回调
 * @author Joker.X
 */
@Composable
private fun UserInfoContent(
    contentPadding: PaddingValues,
    onLogoutClick: () -> Unit = {},
) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        pagePadding = PaddingValues(SpacePaddingLarge),
    ) {
        item {
            Button(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.user_logout))
            }
        }
    }
}

/**
 * 用户信息页浅色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun UserInfoScreenPreview() {
    AppTheme {
        UserInfoScreen()
    }
}

/**
 * 用户信息页深色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun UserInfoScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        UserInfoScreen()
    }
}
