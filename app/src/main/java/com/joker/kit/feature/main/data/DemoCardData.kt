package com.joker.kit.feature.main.data

import com.joker.kit.R
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
            titleResId = R.string.network_demo_title,
            descriptionResId = R.string.network_demo_description,
            navigateAction = { DemoNavigator.toNetworkDemo() }
        ),
        DemoCardInfo(
            titleResId = R.string.network_list_title,
            descriptionResId = R.string.network_list_description,
            navigateAction = { DemoNavigator.toNetworkListDemo() }
        ),
        DemoCardInfo(
            titleResId = R.string.database_title,
            descriptionResId = R.string.database_card_description,
            navigateAction = { DemoNavigator.toDatabase() }
        ),
        DemoCardInfo(
            titleResId = R.string.storage_title,
            descriptionResId = R.string.storage_card_description,
            navigateAction = { DemoNavigator.toLocalStorage() }
        ),
        DemoCardInfo(
            titleResId = R.string.state_management_title,
            descriptionResId = R.string.state_card_description,
            navigateAction = { DemoNavigator.toStateManagement() }
        ),
        DemoCardInfo(
            titleResId = R.string.network_request_title,
            descriptionResId = R.string.network_request_card_description,
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
            titleResId = R.string.navigation_args_title,
            descriptionResId = R.string.navigation_args_card_description,
            navigateAction = { DemoNavigator.toNavigationWithArgs(goodsId = 123) }
        ),
        DemoCardInfo(
            titleResId = R.string.navigation_result_title,
            descriptionResId = R.string.navigation_result_card_description,
            navigateAction = { DemoNavigator.toNavigationResult() }
        ),
        DemoCardInfo(
            titleResId = R.string.navigation_guard_title,
            descriptionResId = R.string.navigation_guard_card_description,
            navigateAction = { UserNavigator.toUserInfo() }
        )
    )

    /**
     * Expand 页签下的演示卡片
     *
     * @author Joker.X
     */
    val expandCards: List<DemoCardInfo> = listOf(
        DemoCardInfo(
            titleResId = R.string.screen_adaptation_title,
            descriptionResId = R.string.screen_adaptation_card_description,
            navigateAction = { DemoNavigator.toScreenAdaptDemo() }
        ),
        DemoCardInfo(
            titleResId = R.string.snackbar_demo_title,
            descriptionResId = R.string.snackbar_demo_card_description,
            navigateAction = { DemoNavigator.toSnackbarDemo() }
        ),
        DemoCardInfo(
            titleResId = R.string.dialog_demo_title,
            descriptionResId = R.string.dialog_demo_card_description,
            navigateAction = { DemoNavigator.toDialogDemo() }
        )
    )
}
