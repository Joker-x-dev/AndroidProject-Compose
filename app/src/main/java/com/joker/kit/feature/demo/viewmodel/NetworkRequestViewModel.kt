package com.joker.kit.feature.demo.viewmodel

import androidx.lifecycle.viewModelScope
import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.data.repository.GoodsRepository
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.result.ResultHandler
import com.joker.kit.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * 网络请求示例页 ViewModel
 *
 * @param goodsRepository 商品仓库
 * @author Joker.X
 */
@HiltViewModel
class NetworkRequestViewModel @Inject constructor(
    private val goodsRepository: GoodsRepository
) : BaseViewModel() {

    /**
     * 商品信息
     *
     * @return 商品信息状态流
     * @author Joker.X
     */
    private val _goods = MutableStateFlow<Goods?>(null)
    val goods: StateFlow<Goods?> = _goods.asStateFlow()

    /**
     * 发起商品信息请求，示例中固定传 id = 1
     *
     * @author Joker.X
     */
    fun onRequestClick() {
        ResultHandler.handleResultWithData(
            scope = viewModelScope,
            flow = goodsRepository.getGoodsInfo("1").asResult(),
            onData = { goods -> _goods.value = goods }
        )
    }
}
