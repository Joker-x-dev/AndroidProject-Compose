package com.joker.kit

import android.app.Application
import android.content.res.Configuration
import com.king.ultraswiperefresh.NestedScrollMode
import com.king.ultraswiperefresh.theme.UltraSwipeRefreshTheme
import com.joker.kit.core.state.UserState
import com.joker.kit.core.util.storage.MMKVUtils
import com.joker.kit.core.util.toast.ToastUtils
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

    override fun onCreate() {
        super.onCreate()
        initToast()
        initLog()
        initMMKV()
        initRefresh()

        // 由于 UserState 依赖 MMKV
        // 所以等待 MMKV 初始化完成以后再初始化 UserState
        userState.initialize()
    }

    /**
     * 初始化 Toast 框架
     *
     * @author Joker.X
     */
    private fun initToast() {
        // 检测当前是否为深色模式
        val isDarkTheme = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        // 初始化Toast，传递深色模式参数
        ToastUtils.init(this, isDarkTheme)
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

    /**
     * 初始化刷新组件配置
     *
     * 统一列表页下拉刷新与上拉加载的交互模式。
     *
     * @author Joker.X
     */
    private fun initRefresh() {
        UltraSwipeRefreshTheme.config = UltraSwipeRefreshTheme.config.copy(
            headerScrollMode = NestedScrollMode.Translate,
            footerScrollMode = NestedScrollMode.Translate
        )
    }

    /**
     * 应用配置变化时调用（如切换深色模式）
     *
     * @param newConfig 新的配置信息
     * @author Joker.X
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // 检测深色模式变化并更新Toast样式
        val isDarkTheme = newConfig.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        // 根据当前主题重新设置Toast样式
        if (isDarkTheme) {
            ToastUtils.setWhiteStyle()
        } else {
            ToastUtils.setBlackStyle()
        }
    }
}
