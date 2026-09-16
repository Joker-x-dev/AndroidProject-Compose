package com.joker.kit.core.data.repository

import com.joker.kit.core.datastore.datasource.settings.AppSettingsStoreDataSource
import com.joker.kit.core.state.AppSettingsUiState
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 应用设置本地存储仓库
 */
@Singleton
class AppSettingsStoreRepository @Inject constructor(
    private val appSettingsStoreDataSource: AppSettingsStoreDataSource,
) {
    /**
     * 保存应用设置
     *
     * @param settings 应用设置状态
    */
    suspend fun saveSettings(settings: AppSettingsUiState) {
        appSettingsStoreDataSource.saveSettings(settings)
    }

    /**
     * 读取应用设置
     *
     * @return 已保存的应用设置，未保存或解析失败时返回 null
    */
    suspend fun getSettings(): AppSettingsUiState? {
        return appSettingsStoreDataSource.getSettings()
    }
}
