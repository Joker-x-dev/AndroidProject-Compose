package com.joker.kit.feature.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.model.entity.Auth
import com.joker.kit.core.model.entity.User
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.state.UserState
import com.joker.kit.core.util.toast.ToastUtils
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
     * 真实项目中，这里会是网络请求，登录成功后会有 token 等信息返回。
     *
     * @author Joker.X
     */
    fun login() {
        viewModelScope.launch {
            val fakeAuth = Auth(
                token = "demo-token",
                refreshToken = "demo-refresh",
                expire = 3600,
                refreshExpire = 7200,
            )
            val fakeUser = User(
                id = 1,
                nickName = "演示用户",
                phone = "18800000000",
            )
            userState.updateUserState(fakeAuth, fakeUser)
            ToastUtils.show("登录成功")
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
            ToastUtils.show("已退出登录")
            navigateBack()
        }
    }
}
