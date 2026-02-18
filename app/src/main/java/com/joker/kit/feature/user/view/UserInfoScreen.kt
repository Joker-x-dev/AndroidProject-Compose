package com.joker.kit.feature.user.view

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
import com.joker.kit.feature.user.viewmodel.UserInfoViewModel

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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserInfoScreen(
    onLogoutClick: () -> Unit = {},
) {
    AppScaffold(
        titleText = "用户信息",
        onBackClick = { navigateBack() },
    ) {
        UserInfoContentView(
            modifier = Modifier.padding(SpacePaddingLarge),
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
private fun UserInfoContentView(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Button(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppText(text = "退出登录")
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
