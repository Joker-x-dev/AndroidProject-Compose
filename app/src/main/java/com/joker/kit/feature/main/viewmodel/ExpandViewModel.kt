package com.joker.kit.feature.main.viewmodel

import com.joker.kit.feature.main.data.DemoCardData
import com.joker.kit.feature.main.model.DemoCardInfo
import com.joker.kit.core.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Expand 页面 ViewModel
 *
 * @author Joker.X
 */
@HiltViewModel
class ExpandViewModel @Inject constructor() : BaseViewModel() {
    /**
     * Expand 卡片源数据
     *
     * @author Joker.X
     */
    private val _cards = MutableStateFlow(DemoCardData.expandCards)

    /**
     * Expand 卡片状态流
     *
     * @author Joker.X
     */
    val cards: StateFlow<List<DemoCardInfo>> = _cards.asStateFlow()
}
