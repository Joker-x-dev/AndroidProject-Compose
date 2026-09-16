package com.joker.kit.feature.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.R
import com.joker.kit.core.model.entity.Auth
import com.joker.kit.core.model.entity.User
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.state.UserState
import com.joker.kit.core.state.AppSnackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 登录页 ViewModel
 *
 * @param userState 全局用户状态
 * @author Joker.X
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userState: UserState
) : BaseViewModel() {

    /**
     * 模拟登录：构造假的 Auth/User，写入 UserState，演示路由拦截放行。
     * 登录请求成功后保存认证信息并发布登录状态。
     *
     * @author Joker.X
     */
    fun login() {
        viewModelScope.launch {
            // 演示登录使用的认证信息
            val fakeAuth = Auth(
                token = "demo-token",
                refreshToken = "demo-refresh",
                expire = 3600,
                refreshExpire = 7200,
            )
            // 演示登录使用的用户信息
            val fakeUser = User(
                id = 1,
                nickName = "演示用户",
                phone = "18800000000",
            )
            userState.updateUserState(fakeAuth, fakeUser)
            AppSnackbar.show(R.string.login_success)
            navigateBack()
        }
    }

    /**
     * 模拟退出登录，清空全局 UserState
     *
     * @author Joker.X
     */
    fun logout() {
        viewModelScope.launch {
            userState.logout()
            AppSnackbar.show(R.string.logout_success)
            navigateBack()
        }
    }
}
