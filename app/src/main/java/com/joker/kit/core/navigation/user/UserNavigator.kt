package com.joker.kit.core.navigation.user

import com.joker.kit.core.navigation.navigate

/**
 * 用户模块导航封装
 *
 * @author Joker.X
 */
object UserNavigator {

    /**
     * 跳转到用户信息页
     *
     * @author Joker.X
     */
    fun toUserInfo() {
        navigate(UserRoutes.Info)
    }
}
