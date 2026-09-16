package com.joker.kit

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.captionBarPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.resolveDarkTheme
import com.joker.kit.core.navigation.AppNavHost
import com.joker.kit.core.navigation.AppNavigator
import com.joker.kit.core.state.AppSettingsState
import com.joker.kit.core.state.AppSnackbarInsets
import com.joker.kit.core.state.LocalAppSettings
import com.joker.kit.core.ui.component.dialog.AppDialogHost
import com.joker.kit.core.ui.component.fps.FpsMonitor
import com.joker.kit.core.ui.component.snackbar.AppSnackbarHost
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.ThemeColorSpec
import top.yukonga.miuix.kmp.theme.ThemePaletteStyle
import top.yukonga.miuix.kmp.squircle.LocalSquircleEnabled
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 应用的主Activity
 * 使用@AndroidEntryPoint注解标记为Hilt依赖注入的入口点
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: AppNavigator

    @Inject
    lateinit var appSettingsState: AppSettingsState

    /**
     * 创建 Activity 界面并挂载应用主题、全局反馈宿主与导航宿主。
     *
     * @param savedInstanceState Activity 重建时保存的实例状态
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // 启动页
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        // 启用边缘到边缘的显示效果
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // 设置Compose内容
        setContent {
            val settings by appSettingsState.uiState.collectAsState()
            val colorSchemeMode = when (settings.themeMode) {
                1 -> ColorSchemeMode.Light
                2 -> ColorSchemeMode.Dark
                3 -> ColorSchemeMode.MonetSystem
                4 -> ColorSchemeMode.MonetLight
                5 -> ColorSchemeMode.MonetDark
                else -> ColorSchemeMode.System
            }
            // 解析应用实际明暗状态，系统栏图标颜色与主题切换保持同步。
            val darkMode = resolveDarkTheme(colorSchemeMode, isSystemInDarkTheme())
            DisposableEffect(darkMode) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkMode },
                    navigationBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkMode },
                )
                // 关闭导航栏对比度保护层，避免部分机型叠加半透明遮罩。
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window.isNavigationBarContrastEnforced = false
                }
                onDispose {}
            }
            val keyColors: List<Color?> = listOf(
                null,
                Color(0xFF6750A4),
                Color(0xFF006A6A),
                Color(0xFF9A4520),
            )
            AppTheme(
                colorSchemeMode = colorSchemeMode,
                keyColor = keyColors.getOrNull(settings.keyColor),
                colorSpec = ThemeColorSpec.entries.getOrElse(settings.colorSpec) { ThemeColorSpec.Spec2021 },
                paletteStyle = ThemePaletteStyle.entries.getOrElse(settings.paletteStyle) { ThemePaletteStyle.TonalSpot },
            ) {
                CompositionLocalProvider(
                    LocalAppSettings provides settings,
                    LocalSquircleEnabled provides settings.enableSquircle,
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // 设置应用的导航宿主，并传入导航管理器和路由注册器
                        AppNavHost(navigator = navigator)
                        // 全局 Snackbar 宿主，覆盖在所有页面之上，并避让主页面底部导航栏
                        AppSnackbarHost(bottomPadding = AppSnackbarInsets.bottomBarHeight)
                        // 全局对话框宿主，覆盖在所有页面之上
                        AppDialogHost()
                        if (settings.showFpsMonitor) {
                            FpsMonitor(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .statusBarsPadding()
                                    .captionBarPadding(),
                            )
                        }
                    }
                }
            }
        }

        // 不让启动界面一直显示
        splashScreen.setKeepOnScreenCondition {
            false
        }
    }
}
