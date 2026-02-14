package com.joker.kit.feature.demo.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.joker.kit.core.navigation.demo.DemoRoutes
import com.joker.kit.feature.demo.view.DatabaseRoute
import com.joker.kit.feature.demo.view.LocalStorageRoute
import com.joker.kit.feature.demo.view.NavigationResultRoute
import com.joker.kit.feature.demo.view.NavigationWithArgsRoute
import com.joker.kit.feature.demo.view.NetworkDemoRoute
import com.joker.kit.feature.demo.view.NetworkListDemoRoute
import com.joker.kit.feature.demo.view.NetworkRequestRoute
import com.joker.kit.feature.demo.view.StateManagementRoute

/**
 * Demo 模块导航图
 *
 * @author Joker.X
 */
fun EntryProviderScope<NavKey>.demoGraph() {
    entry<DemoRoutes.NetworkDemo> {
        NetworkDemoRoute()
    }
    entry<DemoRoutes.NetworkListDemo> {
        NetworkListDemoRoute()
    }
    entry<DemoRoutes.Database> {
        DatabaseRoute()
    }
    entry<DemoRoutes.LocalStorage> {
        LocalStorageRoute()
    }
    entry<DemoRoutes.StateManagement> {
        StateManagementRoute()
    }
    entry<DemoRoutes.NetworkRequest> {
        NetworkRequestRoute()
    }
    entry<DemoRoutes.NavigationWithArgs> { key ->
        NavigationWithArgsRoute(navKey = key)
    }
    entry<DemoRoutes.NavigationResult> {
        NavigationResultRoute()
    }
}
