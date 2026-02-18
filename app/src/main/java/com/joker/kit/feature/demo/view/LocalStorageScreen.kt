package com.joker.kit.feature.demo.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.ShapeMedium
import com.joker.kit.core.designsystem.theme.SpaceHorizontalSmall
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpacePaddingMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.model.entity.User
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.core.ui.component.text.TextSize
import com.joker.kit.core.ui.component.text.TextType
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
    // 用户 ID 输入状态
    val userId by viewModel.userId.collectAsState()
    // 昵称输入状态
    val nickName by viewModel.nickName.collectAsState()
    // 头像链接输入状态
    val avatar by viewModel.avatar.collectAsState()
    // 当前已保存的用户信息
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
@OptIn(ExperimentalMaterial3Api::class)
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
        titleText = "本地存储",
        onBackClick = { navigateBack() },
    ) {
        LocalStorageContent(
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
 * 本地存储内容视图
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(SpacePaddingMedium),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalLarge)
    ) {
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
            onReloadUser = onReloadUser
        )
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
        shape = ShapeMedium
    ) {
        Column(
            modifier = Modifier.padding(SpacePaddingLarge),
            verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
        ) {
            AppText(text = "UserInfoStoreDataSource 示例", size = TextSize.TITLE_MEDIUM)
            AppText(
                text = "保存用户 id / 昵称 / 头像，并可随时清理与重读。",
                type = TextType.TERTIARY,
                size = TextSize.BODY_MEDIUM
            )

            OutlinedTextField(
                value = userId,
                onValueChange = onUserIdChange,
                label = { Text("用户 ID (数字)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nickName,
                onValueChange = onNickNameChange,
                label = { Text("昵称") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = avatar,
                onValueChange = onAvatarChange,
                label = { Text("头像链接 (可选)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SpaceHorizontalSmall, Alignment.End)
            ) {
                TextButton(onClick = onReloadUser) { Text("重新读取") }
                TextButton(onClick = onClearUser) { Text("清除") }
                Button(onClick = onSaveUser, enabled = userId.isNotBlank()) {
                    Text("保存用户")
                }
            }

            HorizontalDivider()

            val userText = user?.let {
                val name = it.nickName ?: "未设置昵称"
                val avatarUrl = it.avatarUrl ?: "无头像"
                "当前用户: id=${it.id}, 昵称=$name\n头像=$avatarUrl"
            } ?: "暂无本地用户信息"

            AppText(
                text = userText,
                type = TextType.SECONDARY,
                size = TextSize.BODY_MEDIUM
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
