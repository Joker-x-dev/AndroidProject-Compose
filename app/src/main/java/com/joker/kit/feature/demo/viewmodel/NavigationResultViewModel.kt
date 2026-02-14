package com.joker.kit.feature.demo.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.navigation.demo.DemoResult
import com.joker.kit.core.navigation.demo.DemoResultKey
import com.joker.kit.core.navigation.popBackStackWithResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 结果回传示例页 ViewModel
 *
 * @author Joker.X
 */
@HiltViewModel
class NavigationResultViewModel @Inject constructor(
) : BaseViewModel() {
    /**
     * 回传结果并返回上一页
     *
     * @author Joker.X
     */
    fun sendResultAndBack() {
        popBackStackWithResult(
            DemoResultKey,
            DemoResult(id = 9527, message = "这是回传的结果")
        )
    }
}
