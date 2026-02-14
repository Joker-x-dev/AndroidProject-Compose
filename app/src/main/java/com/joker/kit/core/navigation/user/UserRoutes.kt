package com.joker.kit.core.navigation.user

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * 用户模块路由
 *
 * @author Joker.X
 */
object UserRoutes {

    /**
     * 用户信息页路由
     *
     * @author Joker.X
     */
    @Serializable
    data object Info : NavKey
}
