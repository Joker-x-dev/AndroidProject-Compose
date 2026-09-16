package com.joker.kit.feature.common.model

import androidx.annotation.StringRes

/**
 * 关于页面的外部链接项。
 *
 * @param title 链接展示标题
 * @param url 链接地址
 * @param description 链接辅助说明
 */
data class AboutLink(
    val title: String,
    val url: String,
    val description: String? = null,
    @StringRes val titleResId: Int? = null,
)
