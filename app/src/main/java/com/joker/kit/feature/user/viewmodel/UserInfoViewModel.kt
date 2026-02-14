package com.joker.kit.feature.user.viewmodel

import androidx.lifecycle.viewModelScope
import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.state.UserState
import com.joker.kit.core.util.toast.ToastUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 用户信息页 ViewModel
 *
 * @param navigator 导航管理器
 * @param userState 全局用户状态
 * @author Joker.X
 */
@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val userState: UserState
) : BaseViewModel() {

    /**
     * 一键退出登录（本地清空）
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
