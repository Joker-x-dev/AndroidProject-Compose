package com.joker.kit.feature.main.data

import com.joker.kit.core.navigation.demo.DemoNavigator
import com.joker.kit.core.navigation.user.UserNavigator
import com.joker.kit.feature.main.model.DemoCardInfo

/**
 * Demo 卡片静态数据源
 *
 * @author Joker.X
 */
object DemoCardData {
    /**
     * Core 页签下的演示卡片
     *
     * @author Joker.X
     */
    val coreCards: List<DemoCardInfo> = listOf(
        DemoCardInfo(
            title = "Network Demo",
            description = "网络状态切换，包含加载、错误、重试等流程。",
            navigateAction = { DemoNavigator.toNetworkDemo() }
        ),
        DemoCardInfo(
            title = "Network List Demo",
            description = "下拉刷新与分页加载的统一列表模板，内置空状态与重试。",
            navigateAction = { DemoNavigator.toNetworkListDemo() }
        ),
        DemoCardInfo(
            title = "数据库",
            description = "Room 的增删改查示例，含简单的列表展示与数据观察。",
            navigateAction = { DemoNavigator.toDatabase() }
        ),
        DemoCardInfo(
            title = "本地存储",
            description = "DataStore / MMKV 的写入与清除示例，演示单值增删改查。",
            navigateAction = { DemoNavigator.toLocalStorage() }
        ),
        DemoCardInfo(
            title = "状态管理",
            description = "全局 DemoCounterState 计数器共享示例，展示跨页面 StateFlow 同步。",
            navigateAction = { DemoNavigator.toStateManagement() }
        ),
        DemoCardInfo(
            title = "网络请求",
            description = "结合 ResultHandler 的通用接口请求、加载状态与错误提示。",
            navigateAction = { DemoNavigator.toNetworkRequest() }
        )
    )

    /**
     * Navigation 页签下的演示卡片
     *
     * @author Joker.X
     */
    val navigationCards: List<DemoCardInfo> = listOf(
        DemoCardInfo(
            title = "带参跳转",
            description = "类型安全路由参数，包含必填/可选参数与目标页接收方式。",
            navigateAction = { DemoNavigator.toNavigationWithArgs(goodsId = 123) }
        ),
        DemoCardInfo(
            title = "结果回传",
            description = "NavigationResultKey 返回数据，包含刷新信号与数据实体回传。",
            navigateAction = { DemoNavigator.toNavigationResult() }
        ),
        DemoCardInfo(
            title = "导航拦截",
            description = "登录拦截流程：未登录跳登录页，登录成功后才能进入用户详情。",
            navigateAction = { UserNavigator.toUserInfo() }
        )
    )
}
