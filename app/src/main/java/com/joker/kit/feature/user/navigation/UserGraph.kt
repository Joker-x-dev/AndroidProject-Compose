package com.joker.kit.feature.user.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.joker.kit.core.navigation.user.UserRoutes
import com.joker.kit.feature.user.view.UserInfoRoute

/**
 * 用户模块导航图
 *
 * @author Joker.X
 */
fun EntryProviderScope<NavKey>.userGraph() {
    entry<UserRoutes.Info> {
        UserInfoRoute()
    }
}
