package com.joker.kit.feature.demo.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.navigation.demo.DemoRoutes
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * 带参跳转示例页 ViewModel
 *
 * @param navKey 导航参数
 * @author Joker.X
 */
@HiltViewModel(assistedFactory = NavigationWithArgsViewModel.Factory::class)
class NavigationWithArgsViewModel @AssistedInject constructor(
    @Assisted private val navKey: DemoRoutes.NavigationWithArgs
) : BaseViewModel() {
    /**
     * 当前商品 ID（用于请求商品详情）
     *
     * @author Joker.X
     */
    val goodsId: Long = navKey.goodsId

    /**
     * Assisted Factory
     *
     * @author Joker.X
     */
    @AssistedFactory
    interface Factory {
        /**
         * 创建 ViewModel 实例
         *
         * @param navKey 导航参数
         * @return ViewModel 实例
         * @author Joker.X
         */
        fun create(navKey: DemoRoutes.NavigationWithArgs): NavigationWithArgsViewModel
    }
}
