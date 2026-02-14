package com.joker.kit.feature.demo.viewmodel

import com.joker.kit.core.base.viewmodel.BaseNetWorkViewModel
import com.joker.kit.core.data.repository.GoodsRepository
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.model.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 网络状态 Demo 页面 ViewModel
 *
 * @param goodsRepository 商品数据仓库
 * @author Joker.X
 */
@HiltViewModel
class NetworkDemoViewModel @Inject constructor(
    private val goodsRepository: GoodsRepository
) : BaseNetWorkViewModel<Goods>() {

    init {
        super.executeRequest()
    }

    /**
     * 重写请求API Flow，获取商品信息
     *
     * @return 商品信息响应流
     * @author Joker.X
     */
    override fun requestApiFlow(): Flow<NetworkResponse<Goods>> {
        return goodsRepository.getGoodsInfo("1")
    }
}
