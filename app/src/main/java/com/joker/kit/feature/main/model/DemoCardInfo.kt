package com.joker.kit.feature.main.model

/**
 * Demo 卡片导航动作
 *
 * @author Joker.X
 */
typealias DemoCardNavigateAction = () -> Unit

/**
 * Demo 卡片信息
 *
 * @param title 标题
 * @param description 描述内容
 * @param navigateAction 跳转动作
 * @author Joker.X
 */
data class DemoCardInfo(
    val title: String,
    val description: String,
    val navigateAction: DemoCardNavigateAction? = null
)
