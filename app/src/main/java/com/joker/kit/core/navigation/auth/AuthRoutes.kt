package com.joker.kit.core.navigation.auth

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * 认证模块路由
 *
 * @author Joker.X
 */
object AuthRoutes {

    /**
     * 登录页路由
     *
     * @author Joker.X
     */
    @Serializable
    data object Login : NavKey
}
