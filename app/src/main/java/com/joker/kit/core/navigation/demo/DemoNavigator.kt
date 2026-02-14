package com.joker.kit.core.navigation.demo

import com.joker.kit.core.navigation.navigate

/**
 * Demo 模块导航封装
 *
 * 统一管理 Demo 模块页面跳转，避免在多个 ViewModel 中
 * 编写重复的中转方法。
 *
 * @author Joker.X
 */
object DemoNavigator {

    /**
     * 跳转到网络状态示例页
     *
     * @author Joker.X
     */
    fun toNetworkDemo() {
        navigate(DemoRoutes.NetworkDemo)
    }

    /**
     * 跳转到网络列表示例页
     *
     * @author Joker.X
     */
    fun toNetworkListDemo() {
        navigate(DemoRoutes.NetworkListDemo)
    }

    /**
     * 跳转到数据库示例页
     *
     * @author Joker.X
     */
    fun toDatabase() {
        navigate(DemoRoutes.Database)
    }

    /**
     * 跳转到本地存储示例页
     *
     * @author Joker.X
     */
    fun toLocalStorage() {
        navigate(DemoRoutes.LocalStorage)
    }

    /**
     * 跳转到状态管理示例页
     *
     * @author Joker.X
     */
    fun toStateManagement() {
        navigate(DemoRoutes.StateManagement)
    }

    /**
     * 跳转到网络请求示例页
     *
     * @author Joker.X
     */
    fun toNetworkRequest() {
        navigate(DemoRoutes.NetworkRequest)
    }

    /**
     * 跳转到带参示例页
     *
     * @param goodsId 商品 ID
     * @author Joker.X
     */
    fun toNavigationWithArgs(goodsId: Long = 0) {
        navigate(DemoRoutes.NavigationWithArgs(goodsId = goodsId))
    }

    /**
     * 跳转到结果回传示例页
     *
     * @author Joker.X
     */
    fun toNavigationResult() {
        navigate(DemoRoutes.NavigationResult)
    }
}
