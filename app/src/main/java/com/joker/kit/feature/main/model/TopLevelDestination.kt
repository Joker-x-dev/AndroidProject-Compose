package com.joker.kit.feature.main.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.joker.kit.R

/**
 * 顶级导航目的地
 *
 * @param titleResId 标题文本资源 ID
 * @param iconResId 图标资源 ID
 * @author Joker.X
 */
enum class TopLevelDestination(
    @param:StringRes val titleResId: Int,
    @param:DrawableRes val iconResId: Int
) {
    CORE(
        titleResId = R.string.nav_core,
        iconResId = R.drawable.ic_tabbar_hardware
    ),
    NAVIGATION(
        titleResId = R.string.nav_navigation,
        iconResId = R.drawable.ic_tabbar_route
    ),
    EXPAND(
        titleResId = R.string.nav_expand,
        iconResId = R.drawable.ic_tabbar_expand
    ),
    SETTING(
        titleResId = R.string.nav_settings,
        iconResId = R.drawable.ic_set
    );
}
