package com.joker.kit.feature.demo.viewmodel

import androidx.lifecycle.viewModelScope
import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.data.repository.UserInfoStoreRepository
import com.joker.kit.core.model.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 本地存储示例页 ViewModel
 *
 * 通过本地仓库 (UserInfoStoreRepository) 演示“用户信息” 的保存 / 读取 / 清除。
 *
 * @param userInfoStoreRepository 用户信息本地存储仓库
 * @author Joker.X
 */
@HiltViewModel
class LocalStorageViewModel @Inject constructor(
    private val userInfoStoreRepository: UserInfoStoreRepository
) : BaseViewModel() {

    /** 用户 id 输入 */
    private val _userId = MutableStateFlow("1")
    val userId: StateFlow<String> = _userId.asStateFlow()

    /** 昵称输入 */
    private val _nickName = MutableStateFlow("")
    val nickName: StateFlow<String> = _nickName.asStateFlow()

    /** 头像输入 */
    private val _avatar = MutableStateFlow("")
    val avatar: StateFlow<String> = _avatar.asStateFlow()

    /** 当前用户信息 */
    private val _userStateFlow = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _userStateFlow.asStateFlow()

    init {
        loadUser()
    }

    /**
     * 用户 id 文本更新
     *
     * @param value 输入的 id 字符串
     * @author Joker.X
     */
    fun onUserIdChange(value: String) {
        _userId.value = value
    }

    /**
     * 用户昵称输入更新
     *
     * @param value 昵称文本
     * @author Joker.X
     */
    fun onNickNameChange(value: String) {
        _nickName.value = value
    }

    /**
     * 头像链接输入更新
     *
     * @param value 头像 URL
     * @author Joker.X
     */
    fun onAvatarChange(value: String) {
        _avatar.value = value
    }

    /**
     * 保存用户信息到本地
     *
     * @author Joker.X
     */
    fun saveUser() {
        viewModelScope.launch {
            val idLong = _userId.value.toLongOrNull() ?: 0L
            val user = User(
                id = idLong,
                nickName = _nickName.value.ifBlank { "未命名" },
                avatarUrl = _avatar.value.ifBlank { null },
                unionid = "demo-unionid-$idLong"
            )
            userInfoStoreRepository.saveUserInfo(user)
            _userStateFlow.value = user
        }
    }

    /**
     * 清除本地用户信息
     *
     * @author Joker.X
     */
    fun clearUser() {
        viewModelScope.launch {
            userInfoStoreRepository.clearUserInfo()
            _userStateFlow.value = null
            _userId.value = "1"
            _nickName.value = ""
            _avatar.value = ""
        }
    }

    /**
     * 重新读取用户信息
     *
     * @author Joker.X
     */
    fun loadUser() {
        viewModelScope.launch {
            val saved = userInfoStoreRepository.getUserInfo()
            _userStateFlow.value = saved
            if (saved != null) {
                _userId.value = saved.id.toString()
                _nickName.value = saved.nickName.orEmpty()
                _avatar.value = saved.avatarUrl.orEmpty()
            }
        }
    }
}
