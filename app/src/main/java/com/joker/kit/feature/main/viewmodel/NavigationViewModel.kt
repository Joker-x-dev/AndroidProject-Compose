package com.joker.kit.feature.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.navigation.demo.DemoResult
import com.joker.kit.core.navigation.demo.DemoResultKey
import com.joker.kit.core.navigation.resultEvents
import com.joker.kit.core.state.UserState
import com.joker.kit.feature.main.data.DemoCardData
import com.joker.kit.feature.main.model.DemoCardInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Navigation 页面 ViewModel
 *
 * @param userState 用户状态
 * @author Joker.X
 */
@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val userState: UserState,
) : BaseViewModel() {
    /**
     * 初始化时监听结果回传
     *
     * @author Joker.X
     */
    init {
        observeDemoResult()
    }

    /**
     * Navigation 卡片源数据
     */
    private val _cards = MutableStateFlow(DemoCardData.navigationCards)

    /**
     * Navigation 卡片状态流
     */
    val cards: StateFlow<List<DemoCardInfo>> = _cards.asStateFlow()

    /**
     * 全局登录状态
     */
    val isLoggedIn: StateFlow<Boolean> = userState.isLoggedIn

    /**
     * Demo 结果状态源
     */
    private val _demoResult = MutableStateFlow<DemoResult?>(null)

    /**
     * Demo 结果状态流
     */
    val demoResult: StateFlow<DemoResult?> = _demoResult.asStateFlow()

    /**
     * 处理回传结果
     *
     * @param result 回传结果
     * @author Joker.X
     */
    fun onResultReceived(result: DemoResult) {
        _demoResult.value = result
    }

    /**
     * 监听结果回传事件
     *
     * @author Joker.X
     */
    private fun observeDemoResult() {
        viewModelScope.launch {
            resultEvents(DemoResultKey).collect { result ->
                onResultReceived(result)
            }
        }
    }
}
