package com.joker.kit.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import com.joker.kit.core.state.DemoCounterState
import com.joker.kit.feature.main.data.DemoCardData
import com.joker.kit.feature.main.model.DemoCardInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Core Demo ViewModel
 *
 * @param counterState 计数器状态
 * @author Joker.X
 */
@HiltViewModel
class CoreDemoViewModel @Inject constructor(
    counterState: DemoCounterState
) : ViewModel() {
    /**
     * Demo 卡片源数据
     *
     * @author Joker.X
     */
    private val _cards = MutableStateFlow(DemoCardData.coreCards)

    /**
     * Demo 卡片状态流
     *
     * @author Joker.X
     */
    val cards: StateFlow<List<DemoCardInfo>> = _cards.asStateFlow()

    /**
     * 全局计数器值
     *
     * @return 计数器状态流
     * @author Joker.X
     */
    val count: StateFlow<Int> = counterState.count
}
