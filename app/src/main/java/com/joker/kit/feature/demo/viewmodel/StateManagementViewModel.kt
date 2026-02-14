package com.joker.kit.feature.demo.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.state.DemoCounterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * 状态管理示例页 ViewModel
 *
 * @param counterState 计数器状态
 * @author Joker.X
 */
@HiltViewModel
class StateManagementViewModel @Inject constructor(
    private val counterState: DemoCounterState
) : BaseViewModel() {

    /**
     * 对外暴露的计数器 StateFlow
     *
     * @return 计数器状态流
     * @author Joker.X
     */
    val count: StateFlow<Int> = counterState.count

    /**
     * +1
     *
     * @author Joker.X
     */
    fun increase() = counterState.increase()

    /**
     * -1
     *
     * @author Joker.X
     */
    fun decrease() = counterState.decrease()

    /**
     * 重置为 0
     *
     * @author Joker.X
     */
    fun reset() = counterState.reset()
}
