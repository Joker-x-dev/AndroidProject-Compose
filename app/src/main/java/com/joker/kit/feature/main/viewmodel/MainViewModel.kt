package com.joker.kit.feature.main.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.state.AppSettingsState
import com.joker.kit.core.state.AppSettingsUiState
import com.joker.kit.feature.main.model.TopLevelDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * 主界面 ViewModel。
 *
 * @param appSettingsState 应用设置共享状态
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val appSettingsState: AppSettingsState,
) : BaseViewModel() {
    /** 主页面使用的应用设置只读状态。 */
    val settings: StateFlow<AppSettingsUiState> = appSettingsState.uiState

    /** 当前底部导航页签索引。 */
    private val _currentPageIndex = MutableStateFlow(0)

    /** 对外暴露的当前底部导航页签索引。 */
    val currentPageIndex: StateFlow<Int> = _currentPageIndex.asStateFlow()

    /**
     * 处理底部导航栏或大屏侧边栏的页签点击。
     *
     * @param index 导航项索引
     */
    fun onNavigationItemClick(index: Int) {
        updateSelectedPage(index)
    }

    /**
     * 处理 Pager 手势完成后的页签同步。
     *
     * @param index Pager 当前页索引
     */
    fun onPageSettled(index: Int) {
        updateSelectedPage(index)
    }

    /** 更新合法的主页面索引。 */
    private fun updateSelectedPage(index: Int) {
        if (index in TopLevelDestination.entries.indices) {
            _currentPageIndex.value = index
        }
    }
}
