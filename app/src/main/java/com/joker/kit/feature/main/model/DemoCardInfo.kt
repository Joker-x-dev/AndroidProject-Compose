package com.joker.kit.feature.main.model

import androidx.annotation.StringRes

/**
 * Demo 卡片导航动作
 *
 * @author Joker.X
 */
typealias DemoCardNavigateAction = () -> Unit

/**
 * Demo 卡片信息
 *
 * @param titleResId 标题资源 ID
 * @param descriptionResId 描述资源 ID
 * @param navigateAction 跳转动作
 * @author Joker.X
 */
data class DemoCardInfo(
    @StringRes val titleResId: Int,
    @StringRes val descriptionResId: Int,
    val navigateAction: DemoCardNavigateAction? = null
)
