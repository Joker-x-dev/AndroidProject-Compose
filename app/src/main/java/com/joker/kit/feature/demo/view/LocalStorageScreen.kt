package com.joker.kit.feature.demo.view

import androidx.compose.ui.res.stringResource
import com.joker.kit.R

import com.joker.kit.core.designsystem.theme.AppColors
import com.joker.kit.core.designsystem.theme.AppTextStyles
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import com.joker.kit.core.designsystem.component.AppColumn
import com.joker.kit.core.designsystem.component.AppRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.RadiusMedium
import com.joker.kit.core.designsystem.theme.SpaceHorizontalSmall
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpacePaddingMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.model.entity.User
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.feature.demo.viewmodel.LocalStorageViewModel

/**
 * 本地存储示例路由
 *
 * @param viewModel Hilt 注入的 LocalStorageViewModel
 * @author Joker.X
 */
@Composable
internal fun LocalStorageRoute(
    viewModel: LocalStorageViewModel = hiltViewModel()
) {
    // 收集用户 ID 输入状态
    val userId by viewModel.userId.collectAsState()
    // 收集昵称输入状态
    val nickName by viewModel.nickName.collectAsState()
    // 收集头像链接输入状态
    val avatar by viewModel.avatar.collectAsState()
    // 收集当前已保存的用户信息
    val user by viewModel.user.collectAsState()

    LocalStorageScreen(
        userId = userId,
        nickName = nickName,
        avatar = avatar,
        user = user,
        onUserIdChange = viewModel::onUserIdChange,
        onNickNameChange = viewModel::onNickNameChange,
        onAvatarChange = viewModel::onAvatarChange,
        onSaveUser = viewModel::saveUser,
        onClearUser = viewModel::clearUser,
        onReloadUser = viewModel::loadUser
    )
}

/**
 * 本地存储示例界面
 *
 * @param userId 用户 ID 输入值
 * @param nickName 用户昵称输入值
 * @param avatar 头像链接输入值
 * @param user 当前已保存的用户信息
 * @param onUserIdChange 用户 ID 文本变化回调
 * @param onNickNameChange 昵称文本变化回调
 * @param onAvatarChange 头像文本变化回调
 * @param onSaveUser 保存用户信息
 * @param onClearUser 清除用户信息
 * @param onReloadUser 重新读取用户信息
 * @author Joker.X
 */
@Composable
internal fun LocalStorageScreen(
    userId: String = "",
    nickName: String = "",
    avatar: String = "",
    user: User? = null,
    onUserIdChange: (String) -> Unit = {},
    onNickNameChange: (String) -> Unit = {},
    onAvatarChange: (String) -> Unit = {},
    onSaveUser: () -> Unit = {},
    onClearUser: () -> Unit = {},
    onReloadUser: () -> Unit = {},
) {
    AppScaffold(
        titleText = stringResource(R.string.storage_title),
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        LocalStorageContent(
            contentPadding = paddingValues,
            userId = userId,
            nickName = nickName,
            avatar = avatar,
            user = user,
            onUserIdChange = onUserIdChange,
            onNickNameChange = onNickNameChange,
            onAvatarChange = onAvatarChange,
            onSaveUser = onSaveUser,
            onClearUser = onClearUser,
            onReloadUser = onReloadUser
        )
    }
}

/**
 * 本地存储页面内容
 *
 * @param userId 用户 ID 输入
 * @param nickName 昵称输入
 * @param avatar 头像输入
 * @param user 已保存的用户信息
 * @param onUserIdChange 用户 ID 更新回调
 * @param onNickNameChange 昵称更新回调
 * @param onAvatarChange 头像更新回调
 * @param onSaveUser 保存用户
 * @param onClearUser 清除用户
 * @param onReloadUser 重新读取用户
 * @author Joker.X
 */
@Composable
private fun LocalStorageContent(
    contentPadding: PaddingValues,
    userId: String,
    nickName: String,
    avatar: String,
    user: User?,
    onUserIdChange: (String) -> Unit,
    onNickNameChange: (String) -> Unit,
    onAvatarChange: (String) -> Unit,
    onSaveUser: () -> Unit,
    onClearUser: () -> Unit,
    onReloadUser: () -> Unit
) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        pagePadding = PaddingValues(SpacePaddingMedium),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalLarge),
    ) {
        item {
            UserCard(
                userId = userId,
                nickName = nickName,
                avatar = avatar,
                user = user,
                onUserIdChange = onUserIdChange,
                onNickNameChange = onNickNameChange,
                onAvatarChange = onAvatarChange,
                onSaveUser = onSaveUser,
                onClearUser = onClearUser,
                onReloadUser = onReloadUser,
            )
        }
    }
}

/**
 * 用户信息存储卡片
 *
 * @param userId 用户 ID 输入
 * @param nickName 用户昵称输入
 * @param avatar 头像链接输入
 * @param user 已保存的用户信息
 * @param onUserIdChange 用户 ID 更新
 * @param onNickNameChange 昵称更新
 * @param onAvatarChange 头像更新
 * @param onSaveUser 保存用户
 * @param onClearUser 清除用户
 * @param onReloadUser 重新读取用户
 * @author Joker.X
 */
@Composable
private fun UserCard(
    userId: String,
    nickName: String,
    avatar: String,
    user: User?,
    onUserIdChange: (String) -> Unit,
    onNickNameChange: (String) -> Unit,
    onAvatarChange: (String) -> Unit,
    onSaveUser: () -> Unit,
    onClearUser: () -> Unit,
    onReloadUser: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = RadiusMedium
    ) {
        AppColumn(
            modifier = Modifier.padding(SpacePaddingLarge),
            verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
        ) {
            Text(text = stringResource(R.string.storage_demo_title), style = AppTextStyles.headline2)
            Text(
                text = stringResource(R.string.storage_demo_description),
                color = AppColors.onSurfaceContainerVariant,
                style = AppTextStyles.body2
            )

            TextField(
                value = userId,
                onValueChange = onUserIdChange,
                label = stringResource(R.string.storage_user_id),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = nickName,
                onValueChange = onNickNameChange,
                label = stringResource(R.string.storage_nickname),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = avatar,
                onValueChange = onAvatarChange,
                label = stringResource(R.string.storage_avatar_url),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )

            AppRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SpaceHorizontalSmall, Alignment.End)
            ) {
                TextButton(text = stringResource(R.string.storage_reload), onClick = onReloadUser)
                TextButton(text = stringResource(R.string.storage_clear), onClick = onClearUser)
                Button(onClick = onSaveUser, enabled = userId.isNotBlank()) {
                    Text(stringResource(R.string.storage_save))
                }
            }

            HorizontalDivider()

            // 本地用户信息展示文案
            val userText = user?.let {
                // 昵称为空时使用的展示文案
                val name = it.nickName ?: stringResource(R.string.storage_no_nickname)
                // 头像为空时使用的展示文案
                val avatarUrl = it.avatarUrl ?: stringResource(R.string.storage_no_avatar)
                stringResource(R.string.storage_user_info_format, it.id, name, avatarUrl)
            } ?: stringResource(R.string.storage_empty_user)

            Text(
                text = userText,
                color = AppColors.onSurfaceSecondary,
                style = AppTextStyles.body2
            )
        }
    }
}

/**
 * 本地存储界面浅色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun LocalStoragePreview() {
    AppTheme {
        LocalStorageScreen(
            userId = "1",
            nickName = "预览用户",
            avatar = "https://example.com/avatar.png",
            user = User(id = 1, nickName = "预览用户", avatarUrl = null)
        )
    }
}

/**
 * 本地存储界面深色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun LocalStoragePreviewDark() {
    AppTheme(darkTheme = true) {
        LocalStorageScreen(
            userId = "1",
            nickName = "预览用户",
            avatar = "https://example.com/avatar.png",
            user = User(id = 1, nickName = "预览用户", avatarUrl = null)
        )
    }
}
