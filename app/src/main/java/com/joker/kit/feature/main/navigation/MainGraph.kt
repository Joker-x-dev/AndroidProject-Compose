package com.joker.kit.feature.main.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.joker.kit.core.navigation.main.MainRoutes
import com.joker.kit.feature.main.view.MainRoute

/**
 * 主模块导航图
 *
 * @author Joker.X
 */
fun EntryProviderScope<NavKey>.mainGraph() {
    entry<MainRoutes.Main> {
        MainRoute()
    }
}
