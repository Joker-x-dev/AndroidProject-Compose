package com.joker.kit.feature.main.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.feature.main.model.LinkItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * About 页面 ViewModel
 *
 * @author Joker.X
 */
@HiltViewModel
class AboutViewModel @Inject constructor() : BaseViewModel() {

    /**
     * 开发者主页
     */
    val developerLink: LinkItem = LinkItem(
        title = "开发作者",
        url = "https://github.com/Joker-x-dev",
        description = "https://github.com/Joker-x-dev"
    )

    /**
     * 项目地址列表
     */
    val projectLinks: List<LinkItem> = listOf(
        LinkItem(
            title = "框架文档",
            url = "https://compose.dusksnow.top",
            description = "https://compose.dusksnow.top"
        ),
        LinkItem(
            title = "GitHub",
            url = "https://github.com/Joker-x-dev/AndroidProject-Compose",
            description = "https://github.com/Joker-x-dev/AndroidProject-Compose"
        ),
        LinkItem(
            title = "Gitee",
            url = "https://gitee.com/Joker-x-dev/AndroidProject-Compose",
            description = "https://gitee.com/Joker-x-dev/AndroidProject-Compose"
        )
    )

    /**
     * 相关资源列表
     */
    val resourceLinks: List<LinkItem> = listOf(
        LinkItem(
            title = "青商城 GitHub",
            url = "https://github.com/Joker-x-dev/CoolMallKotlin",
            description = "https://github.com/Joker-x-dev/CoolMallKotlin"
        ),
        LinkItem(
            title = "青商城 Gitee",
            url = "https://gitee.com/Joker-x-dev/CoolMallKotlin",
            description = "https://gitee.com/Joker-x-dev/CoolMallKotlin"
        )
    )
}
