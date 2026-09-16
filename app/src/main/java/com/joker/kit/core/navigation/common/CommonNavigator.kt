package com.joker.kit.core.navigation.common

import com.joker.kit.core.navigation.navigate

/**
 * Common 功能域导航入口。
 */
object CommonNavigator {
    /**
     * 打开 About 页面。
     */
    fun toAbout() {
        navigate(CommonRoutes.About)
    }
}
