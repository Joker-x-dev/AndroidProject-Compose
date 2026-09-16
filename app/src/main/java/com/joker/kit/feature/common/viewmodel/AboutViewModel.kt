package com.joker.kit.feature.common.viewmodel

import com.joker.kit.R
import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.feature.common.model.AboutLink
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 关于页面 ViewModel。
 *
 * 页面展示的开发者、项目和相关资源链接由 ViewModel 统一提供，页面只负责渲染和转发点击事件。
 */
@HiltViewModel
class AboutViewModel @Inject constructor() : BaseViewModel() {

    /** 开发者主页链接。 */
    val developerLink: AboutLink = AboutLink(
        title = "Joker.X",
        url = "https://github.com/Joker-x-dev",
        description = "joker.x.dev@gmail.com",
    )

    /** 项目文档和代码仓库链接。 */
    val projectLinks: List<AboutLink> = listOf(
        AboutLink(
            title = "",
            titleResId = R.string.about_project_documentation,
            url = "https://compose.dusksnow.top",
            description = "compose.dusksnow.top",
        ),
        AboutLink(
            title = "GitHub",
            url = "https://github.com/Joker-x-dev/AndroidProject-Compose",
            description = "Joker-x-dev",
        ),
        AboutLink(
            title = "Gitee",
            url = "https://gitee.com/Joker-x-dev/AndroidProject-Compose",
            description = "Joker-x-dev",
        ),
    )

    /** 相关项目资源链接。 */
    val resourceLinks: List<AboutLink> = listOf(
        AboutLink(
            title = "",
            titleResId = R.string.about_cool_mall_github,
            url = "https://github.com/Joker-x-dev/CoolMallKotlin",
        ),
        AboutLink(
            title = "",
            titleResId = R.string.about_cool_mall_gitee,
            url = "https://gitee.com/Joker-x-dev/CoolMallKotlin",
        ),
    )
}
