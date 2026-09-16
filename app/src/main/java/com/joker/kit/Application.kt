package com.joker.kit

import android.app.Application
import com.joker.kit.core.state.AppDialogState
import com.joker.kit.core.state.AppSnackbar
import com.joker.kit.core.state.UserState
import com.joker.kit.core.state.AppSettingsState
import com.joker.kit.core.util.storage.MMKVUtils
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * 全局Application
 *
 * @author Joker.X
 */
@HiltAndroidApp
class Application : Application() {

    // 注入全局用户状态管理器
    @Inject
    lateinit var userState: UserState

    @Inject
    lateinit var appSettingsState: AppSettingsState

    override fun onCreate() {
        super.onCreate()
        // 绑定全局 Snackbar 与全局 Dialog 的应用上下文，供字符串资源解析使用
        AppSnackbar.initialize(this)
        AppDialogState.initialize(this)
        initLog()
        initMMKV()

        // 由于 UserState 依赖 MMKV
        // 所以等待 MMKV 初始化完成以后再初始化 UserState
        userState.initialize()
        appSettingsState.initialize()
    }

    /**
     * 初始化 Log 框架
     *
     * @author Joker.X
     */
    private fun initLog() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    /**
     * 初始化 MMKV 框架
     *
     * @author Joker.X
     */
    private fun initMMKV() {
        MMKVUtils.init(this)
    }
}
