package com.joker.kit.feature.main.model

import androidx.annotation.DrawableRes
import com.joker.kit.R

/**
 * 顶级导航目的地
 *
 * @param titleText 标题文本
 * @param iconResId 图标资源 ID
 * @author Joker.X
 */
enum class TopLevelDestination(
    val titleText: String,
    @param:DrawableRes val iconResId: Int
) {
    CORE(
        titleText = "核心",
        iconResId = R.drawable.ic_tabbar_hardware
    ),
    NAVIGATION(
        titleText = "导航",
        iconResId = R.drawable.ic_tabbar_route
    ),
    EXPAND(
        titleText = "扩展",
        iconResId = R.drawable.ic_tabbar_expand
    ),
    ABOUT(
        titleText = "关于",
        iconResId = R.drawable.ic_tabbar_team
    );
}
