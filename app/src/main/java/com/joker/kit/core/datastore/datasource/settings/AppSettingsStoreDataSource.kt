package com.joker.kit.core.datastore.datasource.settings

import com.joker.kit.core.state.AppSettingsUiState

/**
 * 应用设置本地数据源
 */
interface AppSettingsStoreDataSource {
    /**
     * 保存应用设置
     *
     * @param settings 应用设置状态
    */
    suspend fun saveSettings(settings: AppSettingsUiState)

    /**
     * 读取应用设置
     *
     * @return 已保存的应用设置，未保存或解析失败时返回 null
    */
    suspend fun getSettings(): AppSettingsUiState?
}
