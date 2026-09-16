package com.joker.kit.feature.common.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.joker.kit.core.navigation.common.CommonRoutes
import com.joker.kit.feature.common.view.AboutRoute

/**
 * Common 功能域导航图。
 */
fun EntryProviderScope<NavKey>.commonGraph() {
    entry<CommonRoutes.About> {
        AboutRoute()
    }
}
