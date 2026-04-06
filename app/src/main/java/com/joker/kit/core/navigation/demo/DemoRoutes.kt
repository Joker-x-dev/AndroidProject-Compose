package com.joker.kit.core.navigation.demo

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Demo 模块路由
 *
 * @author Joker.X
 */
object DemoRoutes {
    /**
     * Network Demo 示例页路由
     *
     * @author Joker.X
     */
    @Serializable
    data object NetworkDemo : NavKey

    /**
     * Network List Demo 示例页路由
     *
     * @author Joker.X
     */
    @Serializable
    data object NetworkListDemo : NavKey

    /**
     * 数据库示例页路由
     *
     * @author Joker.X
     */
    @Serializable
    data object Database : NavKey

    /**
     * 本地存储示例页路由
     *
     * @author Joker.X
     */
    @Serializable
    data object LocalStorage : NavKey

    /**
     * 状态管理示例页路由
     *
     * @author Joker.X
     */
    @Serializable
    data object StateManagement : NavKey

    /**
     * 通用网络请求示例页路由
     *
     * @author Joker.X
     */
    @Serializable
    data object NetworkRequest : NavKey

    /**
     * 带参跳转示例页路由
     *
     * @param goodsId 商品 ID
     * @author Joker.X
     */
    @Serializable
    data class NavigationWithArgs(
        val goodsId: Long,
    ) : NavKey

    /**
     * 结果回传示例页路由
     *
     * @author Joker.X
     */
    @Serializable
    data object NavigationResult : NavKey

    /**
     * 屏幕适配示例页路由
     *
     * @author Joker.X
     */
    @Serializable
    data object ScreenAdaptDemo : NavKey
}
