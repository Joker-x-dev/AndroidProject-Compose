package com.joker.kit.feature.main.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.navigation.common.CommonNavigator
import com.joker.kit.core.state.AppSettingsState
import com.joker.kit.core.state.AppSettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * 设置页面状态处理
 */
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val appSettingsState: AppSettingsState,
) : BaseViewModel() {
    /**
     * 设置页面只读状态
    */
    val uiState: StateFlow<AppSettingsUiState> = appSettingsState.uiState

    /**
     * 更新主题模式
     *
     * @param value Miuix 主题模式索引
    */
    fun updateThemeMode(value: Int) {
        appSettingsState.updateThemeMode(value)
    }

    /**
     * 更新底部导航显示模式
     *
     * @param value Miuix 导航栏显示模式索引
    */
    fun updateNavigationBarMode(value: Int) {
        appSettingsState.updateNavigationBarMode(value)
    }

    /**
     * 更新底部导航栏样式
     *
     * @param value 底部导航栏样式索引
     */
    fun updateNavigationBarStyle(value: Int) {
        appSettingsState.updateNavigationBarStyle(value)
    }

    /**
     * 更新悬浮导航栏样式
     *
     * @param value 悬浮导航栏样式索引
     */
    fun updateFloatingNavigationBarStyle(value: Int) {
        appSettingsState.updateFloatingNavigationBarStyle(value)
    }

    /**
     * 更新悬浮导航栏位置
     *
     * @param value 悬浮导航栏位置索引
     */
    fun updateFloatingNavigationBarPosition(value: Int) {
        appSettingsState.updateFloatingNavigationBarPosition(value)
    }

    /**
     * 更新动态主题主色。
     *
     * @param value 主题主色的整数表示
     */
    fun updateKeyColor(value: Int) = appSettingsState.updateKeyColor(value)

    /**
     * 更新动态主题调色板样式。
     *
     * @param value Miuix 调色板样式索引
     */
    fun updatePaletteStyle(value: Int) = appSettingsState.updatePaletteStyle(value)

    /**
     * 更新动态主题颜色规范。
     *
     * @param value Miuix 颜色规范索引
     */
    fun updateColorSpec(value: Int) = appSettingsState.updateColorSpec(value)

    /**
     * 更新顶部栏模糊样式。
     *
     * @param value 顶部栏模糊样式索引
     */
    fun updateTopAppBarBlurStyle(value: Int) = appSettingsState.updateTopAppBarBlurStyle(value)

    /**
     * 更新超椭圆形状开关。
     *
     * @param enabled 是否启用超椭圆形状
     */
    fun setEnableSquircle(enabled: Boolean) {
        appSettingsState.updateDisplaySettings(enableSquircle = enabled)
    }

    /**
     * 更新模糊效果开关。
     *
     * @param enabled 是否启用模糊效果
     */
    fun setEnableBlur(enabled: Boolean) {
        appSettingsState.updateDisplaySettings(enableBlur = enabled)
    }

    /**
     * 更新实时帧率监控显示状态。
     *
     * @param enabled 是否显示实时帧率监控
     */
    fun setShowFpsMonitor(enabled: Boolean) {
        appSettingsState.updateDisplaySettings(showFpsMonitor = enabled)
    }

    /**
     * 更新滚动边缘触觉反馈开关。
     *
     * @param enabled 是否启用滚动边缘触觉反馈
     */
    fun setEnableScrollEndHaptic(enabled: Boolean) {
        appSettingsState.updateDisplaySettings(enableScrollEndHaptic = enabled)
    }

    /**
     * 打开 About 页面。
     */
    fun openAbout() {
        CommonNavigator.toAbout()
    }
}
