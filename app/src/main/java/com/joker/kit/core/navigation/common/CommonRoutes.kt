package com.joker.kit.core.navigation.common

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Common 功能域路由。
 */
object CommonRoutes {
    /**
     * About 页面路由。
     */
    @Serializable
    data object About : NavKey

}
