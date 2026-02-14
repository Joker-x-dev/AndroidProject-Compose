package com.joker.kit.feature.main.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * 主界面 ViewModel
 *
 * @author Joker.X
 */
@HiltViewModel
class MainViewModel @Inject constructor(
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /**
     * 切换底部导航 tab
     *
     * @param tab 目标 Tab
     * @author Joker.X
     */
    fun selectTab(tab: MainTab) {
        if (tab == _uiState.value.currentTab) return
        _uiState.value = _uiState.value.copy(currentTab = tab)
    }
}

/**
 * Main 页面 UI 状态
 *
 * @param currentTab 当前底部栏 tab
 * @author Joker.X
 */
data class MainUiState(
    val currentTab: MainTab = MainTab.Core
)

/**
 * Main 页面底部栏 Tab
 *
 * @param title Tab 标题
 * @author Joker.X
 */
enum class MainTab(val title: String) {
    Core("Core"),
    Navigation("Navigation");

    val index: Int get() = ordinal

    companion object {
        val allTabs: List<MainTab> = values().toList()

        /**
         * 根据索引获取 Tab
         *
         * @param index 底部栏索引
         * @return 对应的 Tab，超出范围返回 Core
         * @author Joker.X
         */
        fun fromIndex(index: Int): MainTab {
            return allTabs.getOrElse(index) { Core }
        }
    }
}
