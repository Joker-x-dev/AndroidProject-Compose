package com.joker.kit.core.navigation.main

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * 主模块路由
 *
 * @author Joker.X
 */
object MainRoutes {

    /**
     * 主框架页路由
     *
     * @author Joker.X
     */
    @Serializable
    data object Main : NavKey
}
