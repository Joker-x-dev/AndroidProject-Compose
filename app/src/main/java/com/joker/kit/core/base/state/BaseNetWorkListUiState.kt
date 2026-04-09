package com.joker.kit.core.base.state

/**
 * 列表页UI状态
 *
 * 封装列表页面的四种状态：加载中、成功、错误和空数据
 *
 * @author Joker.X
 */
sealed class BaseNetWorkListUiState {
    /**
     * 加载中状态
     */
    data object Loading : BaseNetWorkListUiState()

    /**
     * 成功状态
     */
    data object Success : BaseNetWorkListUiState()

    /**
     * 错误状态
     */
    data object Error : BaseNetWorkListUiState()

    /**
     * 空数据状态
     */
    data object Empty : BaseNetWorkListUiState()
}
