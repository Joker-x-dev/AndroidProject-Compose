package com.joker.kit.feature.auth.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.joker.kit.core.navigation.auth.AuthRoutes
import com.joker.kit.feature.auth.view.LoginRoute

/**
 * 认证模块导航图
 *
 * @author Joker.X
 */
fun EntryProviderScope<NavKey>.authGraph() {
    entry<AuthRoutes.Login> {
        LoginRoute()
    }
}
