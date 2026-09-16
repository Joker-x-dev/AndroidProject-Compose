package com.joker.kit.feature.main.view

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.stringArrayResource
import com.joker.kit.R

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.annotation.ScreenPreview
import com.joker.kit.core.annotation.ScreenPreviewDark
import com.joker.kit.core.designsystem.component.SpaceVerticalMedium
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.SpaceHorizontalMedium
import com.joker.kit.core.state.AppSettingsUiState
import com.joker.kit.core.ui.adaptive.isLG
import com.joker.kit.core.ui.adaptive.isMD
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.feature.main.component.mainPageContentPadding
import com.joker.kit.feature.main.viewmodel.SettingViewModel
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.blur.isRuntimeShaderSupported
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference

/**
 * 设置页面路由
 *
 * @param bottomInnerPadding 主页面底部导航栏占用高度
 * @param viewModel 设置页面 ViewModel
 */
@Composable
internal fun SettingRoute(
    bottomInnerPadding: Dp = 0.dp,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    // 收集应用设置状态，驱动设置项和导航栏配置展示。
    val settings by viewModel.uiState.collectAsState()
    SettingScreen(
        bottomInnerPadding = bottomInnerPadding,
        settings = settings,
        onThemeModeChange = viewModel::updateThemeMode,
        onNavigationBarModeChange = viewModel::updateNavigationBarMode,
        onNavigationBarStyleChange = viewModel::updateNavigationBarStyle,
        onFloatingNavigationBarStyleChange = viewModel::updateFloatingNavigationBarStyle,
        onFloatingNavigationBarPositionChange = viewModel::updateFloatingNavigationBarPosition,
        onKeyColorChange = viewModel::updateKeyColor,
        onPaletteStyleChange = viewModel::updatePaletteStyle,
        onColorSpecChange = viewModel::updateColorSpec,
        onTopAppBarBlurStyleChange = viewModel::updateTopAppBarBlurStyle,
        onEnableSquircleChange = viewModel::setEnableSquircle,
        onEnableBlurChange = viewModel::setEnableBlur,
        onShowFpsMonitorChange = viewModel::setShowFpsMonitor,
        onEnableScrollEndHapticChange = viewModel::setEnableScrollEndHaptic,
        onAboutClick = viewModel::openAbout,
    )
}

/**
 * 设置页面
 *
 * @param bottomInnerPadding 主页面底部导航栏占用高度
 * @param settings 应用设置状态
 * @param onThemeModeChange 主题模式变更回调
 * @param onNavigationBarModeChange 导航栏模式变更回调
 * @param onNavigationBarStyleChange 导航栏样式变更回调
 * @param onFloatingNavigationBarStyleChange 悬浮导航栏样式变更回调
 * @param onFloatingNavigationBarPositionChange 悬浮导航栏位置变更回调
 * @param onEnableSquircleChange 超椭圆形状开关回调
 * @param onEnableBlurChange 模糊效果开关回调
 * @param onEnableScrollEndHapticChange 滚动边缘触觉开关回调
 * @param onAboutClick About 页面点击回调
 */
@Composable
internal fun SettingScreen(
    bottomInnerPadding: Dp = 0.dp,
    settings: AppSettingsUiState = AppSettingsUiState(),
    onThemeModeChange: (Int) -> Unit = {},
    onNavigationBarModeChange: (Int) -> Unit = {},
    onNavigationBarStyleChange: (Int) -> Unit = {},
    onFloatingNavigationBarStyleChange: (Int) -> Unit = {},
    onFloatingNavigationBarPositionChange: (Int) -> Unit = {},
    onKeyColorChange: (Int) -> Unit = {},
    onPaletteStyleChange: (Int) -> Unit = {},
    onColorSpecChange: (Int) -> Unit = {},
    onTopAppBarBlurStyleChange: (Int) -> Unit = {},
    onEnableSquircleChange: (Boolean) -> Unit = {},
    onEnableBlurChange: (Boolean) -> Unit = {},
    onShowFpsMonitorChange: (Boolean) -> Unit = {},
    onEnableScrollEndHapticChange: (Boolean) -> Unit = {},
    onAboutClick: () -> Unit = {},
) {
    AppScaffold(
        titleText = stringResource(R.string.nav_settings),
        showBackIcon = false,
        contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout)
            .only(WindowInsetsSides.Horizontal),
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        SettingContent(
            settings = settings,
            contentPadding = paddingValues,
            bottomInnerPadding = bottomInnerPadding,
            onThemeModeChange = onThemeModeChange,
            onNavigationBarModeChange = onNavigationBarModeChange,
            onNavigationBarStyleChange = onNavigationBarStyleChange,
            onFloatingNavigationBarStyleChange = onFloatingNavigationBarStyleChange,
            onFloatingNavigationBarPositionChange = onFloatingNavigationBarPositionChange,
            onKeyColorChange = onKeyColorChange,
            onPaletteStyleChange = onPaletteStyleChange,
            onColorSpecChange = onColorSpecChange,
            onTopAppBarBlurStyleChange = onTopAppBarBlurStyleChange,
            onEnableSquircleChange = onEnableSquircleChange,
            onEnableBlurChange = onEnableBlurChange,
            onShowFpsMonitorChange = onShowFpsMonitorChange,
            onEnableScrollEndHapticChange = onEnableScrollEndHapticChange,
            onAboutClick = onAboutClick,
        )
    }
}

/**
 * 设置页面内容。
 *
 * @param settings 应用设置状态
 * @param contentPadding 页面 Scaffold 内容边距
 * @param bottomInnerPadding 主页面底部导航栏占用高度
 * @param onThemeModeChange 主题模式变更回调
 * @param onNavigationBarModeChange 导航栏模式变更回调
 * @param onNavigationBarStyleChange 导航栏样式变更回调
 * @param onFloatingNavigationBarStyleChange 悬浮导航栏样式变更回调
 * @param onFloatingNavigationBarPositionChange 悬浮导航栏位置变更回调
 * @param onKeyColorChange 动态主题主色变更回调
 * @param onPaletteStyleChange 动态主题调色板变更回调
 * @param onColorSpecChange 动态主题颜色规范变更回调
 * @param onTopAppBarBlurStyleChange 顶部栏模糊样式变更回调
 * @param onEnableSquircleChange 超椭圆形状开关回调
 * @param onEnableBlurChange 模糊效果开关回调
 * @param onEnableScrollEndHapticChange 滚动边缘触觉开关回调
 * @param onAboutClick 关于页面点击回调
 */
@Composable
private fun SettingContent(
    settings: AppSettingsUiState,
    contentPadding: PaddingValues,
    bottomInnerPadding: Dp,
    onThemeModeChange: (Int) -> Unit,
    onNavigationBarModeChange: (Int) -> Unit,
    onNavigationBarStyleChange: (Int) -> Unit,
    onFloatingNavigationBarStyleChange: (Int) -> Unit,
    onFloatingNavigationBarPositionChange: (Int) -> Unit,
    onKeyColorChange: (Int) -> Unit,
    onPaletteStyleChange: (Int) -> Unit,
    onColorSpecChange: (Int) -> Unit,
    onTopAppBarBlurStyleChange: (Int) -> Unit,
    onEnableSquircleChange: (Boolean) -> Unit,
    onEnableBlurChange: (Boolean) -> Unit,
    onShowFpsMonitorChange: (Boolean) -> Unit,
    onEnableScrollEndHapticChange: (Boolean) -> Unit,
    onAboutClick: () -> Unit,
) {
    val themeItems = stringArrayResource(R.array.settings_theme_items).toList()
    val navigationStyleItems = stringArrayResource(R.array.settings_navigation_style_items).toList()
    val navigationItems = stringArrayResource(R.array.settings_navigation_items).toList()
    val floatingStyleItems = stringArrayResource(R.array.settings_floating_style_items).toList()
    val floatingPositionItems = stringArrayResource(R.array.settings_floating_position_items).toList()
    val keyColorItems = stringArrayResource(R.array.settings_key_color_items).toList()
    val paletteStyleItems = stringArrayResource(R.array.settings_palette_style_items).toList()
    val colorSpecItems = stringArrayResource(R.array.settings_color_spec_items).toList()
    val blurStyleItems = stringArrayResource(R.array.settings_blur_style_items).toList()
    val isWideScreen = isMD() || isLG()

    val pagePadding = mainPageContentPadding(contentPadding, bottomInnerPadding)
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        // 区块标题使用 SmallTitle 默认水平内边距，卡片单独保留 Miuix 12dp 边距。
        contentPadding = PaddingValues(
            top = pagePadding.calculateTopPadding(),
            bottom = pagePadding.calculateBottomPadding(),
        ),
    ) {
        item {
            SmallTitle(text = stringResource(R.string.settings_theme))
        }
        item {
            Card(modifier = Modifier.padding(horizontal = SpaceHorizontalMedium)) {
                OverlayDropdownPreference(
                    title = stringResource(R.string.settings_theme_mode),
                    summary = stringResource(R.string.settings_theme_mode_summary),
                    items = themeItems,
                    selectedIndex = settings.themeMode,
                    onSelectedIndexChange = onThemeModeChange,
                )
                AnimatedVisibility(visible = settings.themeMode in 3..5) {
                    OverlayDropdownPreference(
                        title = stringResource(R.string.settings_key_color),
                        summary = stringResource(R.string.settings_key_color_summary),
                        items = keyColorItems,
                        selectedIndex = settings.keyColor.coerceIn(keyColorItems.indices),
                        onSelectedIndexChange = onKeyColorChange,
                    )
                }
                AnimatedVisibility(visible = settings.themeMode in 3..5 && settings.keyColor > 0) {
                    Column {
                        OverlayDropdownPreference(
                            title = stringResource(R.string.settings_palette_style),
                            summary = stringResource(R.string.settings_palette_style_summary),
                            items = paletteStyleItems,
                            selectedIndex = settings.paletteStyle.coerceIn(paletteStyleItems.indices),
                            onSelectedIndexChange = onPaletteStyleChange,
                        )
                        OverlayDropdownPreference(
                            title = stringResource(R.string.settings_color_spec),
                            summary = stringResource(R.string.settings_color_spec_summary),
                            items = colorSpecItems,
                            selectedIndex = settings.colorSpec.coerceIn(colorSpecItems.indices),
                            onSelectedIndexChange = onColorSpecChange,
                        )
                    }
                }
                SwitchPreference(
                    title = stringResource(R.string.settings_fps),
                    summary = stringResource(R.string.settings_fps_summary),
                    checked = settings.showFpsMonitor,
                    onCheckedChange = onShowFpsMonitorChange,
                )
                AnimatedVisibility(visible = isRuntimeShaderSupported()) {
                    SwitchPreference(
                        title = stringResource(R.string.settings_squircle),
                        summary = stringResource(R.string.settings_squircle_summary),
                        checked = settings.enableSquircle,
                        onCheckedChange = onEnableSquircleChange,
                    )
                }
                AnimatedVisibility(visible = isRuntimeShaderSupported()) {
                    SwitchPreference(
                        title = stringResource(R.string.settings_blur),
                        summary = stringResource(R.string.settings_blur_summary),
                        checked = settings.enableBlur,
                        onCheckedChange = onEnableBlurChange,
                    )
                }
                SwitchPreference(
                    title = stringResource(R.string.settings_haptic),
                    summary = stringResource(R.string.settings_haptic_summary),
                    checked = settings.enableScrollEndHaptic,
                    onCheckedChange = onEnableScrollEndHapticChange,
                )
                AnimatedVisibility(visible = settings.enableBlur && isRuntimeShaderSupported()) {
                    OverlayDropdownPreference(
                        title = stringResource(R.string.settings_top_bar_blur),
                        summary = stringResource(R.string.settings_top_bar_blur_summary),
                        items = blurStyleItems,
                        selectedIndex = settings.topAppBarBlurStyle,
                        onSelectedIndexChange = onTopAppBarBlurStyleChange,
                    )
                }
                if (!isWideScreen) {
                    OverlayDropdownPreference(
                        title = stringResource(R.string.settings_navigation_style),
                        summary = navigationStyleItems[settings.navigationBarStyle],
                        items = navigationStyleItems,
                        selectedIndex = settings.navigationBarStyle,
                        onSelectedIndexChange = onNavigationBarStyleChange,
                    )
                    AnimatedVisibility(visible = settings.navigationBarStyle == 0) {
                        OverlayDropdownPreference(
                            title = stringResource(R.string.settings_navigation_mode),
                            summary = navigationItems[settings.navigationBarMode],
                            items = navigationItems,
                            selectedIndex = settings.navigationBarMode,
                            onSelectedIndexChange = onNavigationBarModeChange,
                        )
                    }
                    AnimatedVisibility(visible = settings.navigationBarStyle == 1) {
                        OverlayDropdownPreference(
                            title = stringResource(R.string.settings_floating_style),
                            summary = floatingStyleItems[settings.floatingNavigationBarStyle],
                            items = floatingStyleItems,
                            selectedIndex = settings.floatingNavigationBarStyle,
                            onSelectedIndexChange = onFloatingNavigationBarStyleChange,
                        )
                    }
                    AnimatedVisibility(
                        visible = settings.navigationBarStyle == 1 &&
                                settings.floatingNavigationBarStyle == 0,
                    ) {
                        OverlayDropdownPreference(
                            title = stringResource(R.string.settings_floating_position),
                            summary = floatingPositionItems[settings.floatingNavigationBarPosition],
                            items = floatingPositionItems,
                            selectedIndex = settings.floatingNavigationBarPosition,
                            onSelectedIndexChange = onFloatingNavigationBarPositionChange,
                        )
                    }
                }
            }
        }
        item {
            SpaceVerticalMedium()
            SmallTitle(text = stringResource(R.string.settings_about))
        }
        item {
            Card(modifier = Modifier.padding(horizontal = SpaceHorizontalMedium)) {
                ArrowPreference(
                    title = stringResource(R.string.settings_about),
                    summary = stringResource(R.string.settings_about_summary),
                    onClick = onAboutClick,
                )
            }
        }
    }

}

/**
 * 设置页面浅色预览
 */
@ScreenPreview
@Composable
private fun SettingScreenPreview() {
    AppTheme {
        SettingScreen()
    }
}

/**
 * 设置页面深色预览
 */
@ScreenPreviewDark
@Composable
private fun SettingScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        SettingScreen()
    }
}
