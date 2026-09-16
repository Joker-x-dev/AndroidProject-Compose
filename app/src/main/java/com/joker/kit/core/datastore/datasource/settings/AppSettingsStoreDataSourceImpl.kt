package com.joker.kit.core.datastore.datasource.settings

import com.joker.kit.core.state.AppSettingsUiState
import com.joker.kit.core.util.storage.MMKVUtils
import jakarta.inject.Inject
import kotlinx.serialization.json.Json

/**
 * 基于 MMKV 的应用设置数据源
 */
class AppSettingsStoreDataSourceImpl @Inject constructor() : AppSettingsStoreDataSource {
    private companion object {
        const val KEY_APP_SETTINGS = "app_settings"
    }

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * 保存应用设置 JSON
     *
     * @param settings 应用设置状态
    */
    override suspend fun saveSettings(settings: AppSettingsUiState) {
        MMKVUtils.putString(KEY_APP_SETTINGS, json.encodeToString(settings))
    }

    /**
     * 读取并解析应用设置 JSON
     *
     * @return 已保存的应用设置，未保存或解析失败时返回 null
    */
    override suspend fun getSettings(): AppSettingsUiState? {
        val value = MMKVUtils.getString(KEY_APP_SETTINGS, "")
        if (value.isEmpty()) return null
        return runCatching { json.decodeFromString<AppSettingsUiState>(value) }.getOrNull()
    }
}
