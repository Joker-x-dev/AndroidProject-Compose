package com.joker.kit.core.navigation

/**
 * 公共导航参数定义
 *
 * 用于沉淀跨模块可复用的导航参数模型，避免在各业务模块重复声明。
 *
 * @author Joker.X
 */
/**
 * 通用 ID 参数
 *
 * 适用于仅需要传递单个 ID 的页面跳转场景。
 *
 * @property id 通用业务 ID
 * @author Joker.X
 */
data class IdParam(
    /**
     * 通用业务 ID
     */
    val id: Long,
)
