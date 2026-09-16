package com.joker.kit.core.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import com.joker.kit.core.data.repository.AppSettingsStoreRepository
import com.joker.kit.core.state.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 应用设置状态
 *
 * 主题模式和导航栏显示模式由此状态统一管理。
 */
@Immutable
@Serializable
data class AppSettingsUiState(
    /**
     * Miuix 主题模式索引：0 系统、1 浅色、2 深色、3 动态系统、4 动态浅色、5 动态深色。
    */
    val themeMode: Int = 0,
    /**
     * Miuix 底部导航显示模式索引：0 图标和文字、1 仅图标、2 仅显示选中标签。
    */
    val navigationBarMode: Int = 0,
    /** 底部导航栏样式索引：0 固定导航栏、1 悬浮导航栏。 */
    val navigationBarStyle: Int = 0,
    /** 悬浮导航栏样式索引：0 普通悬浮、1 液态玻璃。 */
    val floatingNavigationBarStyle: Int = 0,
    /** 悬浮导航栏位置索引：0 居中、1 左侧、2 右侧。 */
    val floatingNavigationBarPosition: Int = 0,
    /** 动态主题主色索引。 */
    val keyColor: Int = 0,
    /** 动态主题调色板样式索引。 */
    val paletteStyle: Int = 0,
    /** 动态主题颜色规范索引。 */
    val colorSpec: Int = 0,
    /** 是否显示实时帧率监控。 */
    val showFpsMonitor: Boolean = false,
    /** 是否启用超椭圆形状。 */
    val enableSquircle: Boolean = true,
    /** 是否启用模糊效果。 */
    val enableBlur: Boolean = true,
    /** 是否启用滚动边缘触觉反馈。 */
    val enableScrollEndHaptic: Boolean = true,
    /** 顶部栏模糊样式索引。 */
    val topAppBarBlurStyle: Int = 0,
)

/** 当前应用设置，供页面骨架读取全局交互配置。 */
val LocalAppSettings = compositionLocalOf { AppSettingsUiState() }

/**
 * 应用设置状态持有者
 *
 * @param appSettingsStoreRepository 应用设置本地存储仓库
 * @param applicationScope 应用级协程作用域
 */
@Singleton
class AppSettingsState @Inject constructor(
    private val appSettingsStoreRepository: AppSettingsStoreRepository,
    @param:ApplicationScope private val applicationScope: CoroutineScope,
) {
    private val _uiState = MutableStateFlow(AppSettingsUiState())
    private val persistenceMutex = Mutex()

    /**
     * 对外暴露的只读应用设置状态。
    */
    val uiState: StateFlow<AppSettingsUiState> = _uiState.asStateFlow()

    /**
     * 从 MMKV 恢复应用设置。
    */
    fun initialize() {
        applicationScope.launch {
            appSettingsStoreRepository.getSettings()?.let { settings ->
                _uiState.value = settings
            }
        }
    }

    /**
     * 更新主题模式
     *
     * @param value Miuix 主题模式索引
    */
    fun updateThemeMode(value: Int) {
        update { it.copy(themeMode = value.coerceIn(0, 5)) }
    }

    /**
     * 更新底部导航显示模式
     *
     * @param value Miuix 导航栏显示模式索引
    */
    fun updateNavigationBarMode(value: Int) {
        update { it.copy(navigationBarMode = value.coerceIn(0, 2)) }
    }

    /**
     * 更新底部导航栏样式
     *
     * @param value 底部导航栏样式索引
     */
    fun updateNavigationBarStyle(value: Int) {
        update { it.copy(navigationBarStyle = value.coerceIn(0, 1)) }
    }

    /**
     * 更新悬浮导航栏样式
     *
     * @param value 悬浮导航栏样式索引
     */
    fun updateFloatingNavigationBarStyle(value: Int) {
        update { it.copy(floatingNavigationBarStyle = value.coerceIn(0, 1)) }
    }

    /**
     * 更新悬浮导航栏位置
     *
     * @param value 悬浮导航栏位置索引
     */
    fun updateFloatingNavigationBarPosition(value: Int) {
        update { it.copy(floatingNavigationBarPosition = value.coerceIn(0, 2)) }
    }

    /** 更新布尔型显示设置。 */
    fun updateDisplaySettings(
        enableSquircle: Boolean? = null,
        enableBlur: Boolean? = null,
        enableScrollEndHaptic: Boolean? = null,
        showFpsMonitor: Boolean? = null,
    ) {
        update {
            it.copy(
                enableSquircle = enableSquircle ?: it.enableSquircle,
                enableBlur = enableBlur ?: it.enableBlur,
                enableScrollEndHaptic = enableScrollEndHaptic ?: it.enableScrollEndHaptic,
                showFpsMonitor = showFpsMonitor ?: it.showFpsMonitor,
            )
        }
    }

    /**
     * 更新动态主题主色。
     *
     * @param value 主题主色的整数表示
     */
    fun updateKeyColor(value: Int) = update { it.copy(keyColor = value.coerceAtLeast(0)) }

    /**
     * 更新动态主题调色板样式。
     *
     * @param value Miuix 调色板样式索引
     */
    fun updatePaletteStyle(value: Int) = update { it.copy(paletteStyle = value.coerceAtLeast(0)) }

    /**
     * 更新动态主题颜色规范。
     *
     * @param value Miuix 颜色规范索引
     */
    fun updateColorSpec(value: Int) = update { it.copy(colorSpec = value.coerceAtLeast(0)) }

    /**
     * 更新顶部栏模糊样式。
     *
     * @param value 顶部栏模糊样式索引
     */
    fun updateTopAppBarBlurStyle(value: Int) = update { it.copy(topAppBarBlurStyle = value.coerceIn(0, 1)) }

    /**
     * 原子更新内存状态并持久化最新快照
     *
     * @param transform 状态转换函数
    */
    private fun update(transform: (AppSettingsUiState) -> AppSettingsUiState) {
        _uiState.update(transform)
        applicationScope.launch {
            persistenceMutex.withLock {
                appSettingsStoreRepository.saveSettings(_uiState.value)
            }
        }
    }
}
