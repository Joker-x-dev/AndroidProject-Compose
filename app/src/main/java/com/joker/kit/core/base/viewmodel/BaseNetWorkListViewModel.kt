package com.joker.kit.core.base.viewmodel

import androidx.lifecycle.viewModelScope
import com.joker.kit.core.base.state.BaseNetWorkListUiState
import com.joker.kit.core.model.network.NetworkPageData
import com.joker.kit.core.model.network.NetworkResponse
import com.joker.kit.core.navigation.NavigationResultKey
import com.joker.kit.core.navigation.RefreshResult
import com.joker.kit.core.navigation.RefreshResultKey
import com.joker.kit.core.navigation.resultEvents
import com.joker.kit.core.result.ResultHandler
import com.joker.kit.core.result.asResult
import com.joker.kit.core.util.time.TimeUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 网络请求列表ViewModel基类
 *
 * 专门处理列表数据的加载、分页、刷新和加载更多功能
 * 封装了常见的列表操作逻辑，简化子类实现
 *
 * @param T 列表项数据类型
 * @author Joker.X
 */
abstract class BaseNetWorkListViewModel<T : Any> : BaseViewModel() {
    /**
     * 刷新结果监听任务
     *
     * 用于保证只注册一次刷新结果监听，避免重复 collect 导致重复刷新和内存浪费。
     * 当该任务不为 null 时，表示当前 ViewModel 已经建立监听。
     */
    private var refreshObserveJob: Job? = null

    /**
     * 当前页码
     */
    protected var currentPage = 1

    /**
     * 每页数量
     */
    protected open val pageSize = 10

    /**
     * 网络请求UI状态
     */
    val _uiState = MutableStateFlow<BaseNetWorkListUiState>(BaseNetWorkListUiState.Loading)
    val uiState: StateFlow<BaseNetWorkListUiState> = _uiState.asStateFlow()

    /**
     * 列表数据
     */
    protected val _listData = MutableStateFlow<List<T>>(emptyList())
    val listData: StateFlow<List<T>> = _listData.asStateFlow()

    /**
     * 下拉刷新状态
     */
    protected val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    /**
     * 上拉加载状态
     */
    protected val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    /**
     * 是否还有更多数据
     */
    protected val _hasMoreData = MutableStateFlow(false)
    val hasMoreData: StateFlow<Boolean> = _hasMoreData.asStateFlow()

    /**
     * 是否启用最少加载时间（320毫秒）
     * 子类可重写此属性以启用最少加载时间功能
     */
    protected open val enableMinLoadingTime: Boolean = false

    /**
     * 请求开始时间，用于计算最少加载时间（仅首次加载）
     */
    private var requestStartTime: Long = 0

    /**
     * 刷新开始时间，用于控制刷新完成态的最短展示时长
     */
    private var refreshStartTime: Long = 0

    /**
     * 加载更多开始时间，用于控制加载更多完成态的最短展示时长
     */
    private var loadMoreStartTime: Long = 0

    /**
     * 子类必须实现此方法，提供分页API请求的Flow
     *
     * @return 返回包含分页数据的Flow
     */
    protected abstract fun requestListData(): Flow<NetworkResponse<NetworkPageData<T>>>

    /**
     * 初始化函数，在子类init块中调用
     */
    protected fun initLoad() {
        loadListData()
    }

    /**
     * 加载列表数据
     */
    protected fun loadListData() {
        val isFirstLoading =
            currentPage == 1 && _listData.value.isEmpty() && !_isRefreshing.value

        // 记录请求开始时间（仅首次加载）并且启用最少加载时间功能
        if (isFirstLoading && enableMinLoadingTime) {
            requestStartTime = System.currentTimeMillis()
        }

        // 设置UI状态 - 仅首次加载显示加载中状态
        if (isFirstLoading) {
            _uiState.value = BaseNetWorkListUiState.Loading
            _isLoadingMore.value = false
        }

        ResultHandler.handleResult(
            showToast = false,
            scope = viewModelScope,
            flow = requestListData().asResult(),
            onSuccess = { response ->
                handleSuccess(response.data)
            },
            onError = { message, exception ->
                handleError(message, exception)
            }
        )
    }

    /**
     * 处理成功响应
     */
    protected open fun handleSuccess(data: NetworkPageData<T>?) {
        val newList = data?.list ?: emptyList()
        val pagination = data?.pagination

        // 计算是否还有下一页数据
        val hasNextPage = if (pagination != null) {
            val total = pagination.total ?: 0
            val size = pagination.size ?: pageSize
            val currentPageNum = pagination.page ?: currentPage

            // 当前页的数据量 * 当前页码 < 总数据量，说明还有下一页
            size * currentPageNum < total
        } else {
            false
        }

        when {
            currentPage == 1 -> {
                // 刷新或首次加载 - 重置列表
                _listData.value = newList

                // 下拉刷新成功时，保持最短展示时长，避免刷新态一闪而过
                if (_isRefreshing.value) {
                    val remainingDuration =
                        TimeUtils.calculateRemainingDuration(refreshStartTime, 500L)
                    if (remainingDuration > 0) {
                        viewModelScope.launch {
                            delay(remainingDuration)
                            setFirstLoadSuccessState(newList, hasNextPage)
                        }
                    } else {
                        setFirstLoadSuccessState(newList, hasNextPage)
                    }
                } else if (enableMinLoadingTime) {
                    // 判断是否需要最少加载时间延迟
                    val minLoadingTime = 320L
                    val remainingDuration =
                        TimeUtils.calculateRemainingDuration(requestStartTime, minLoadingTime)

                    if (remainingDuration > 0) {
                        // 延迟设置成功状态
                        viewModelScope.launch {
                            delay(remainingDuration)
                            setFirstLoadSuccessState(newList, hasNextPage)
                        }
                    } else {
                        setFirstLoadSuccessState(newList, hasNextPage)
                    }
                } else {
                    setFirstLoadSuccessState(newList, hasNextPage)
                }
            }

            else -> {
                val remainingDuration =
                    TimeUtils.calculateRemainingDuration(loadMoreStartTime, 500L)

                viewModelScope.launch {
                    if (remainingDuration > 0) {
                        delay(remainingDuration)
                    }
                    _listData.value += newList
                    _isLoadingMore.value = false
                    _hasMoreData.value = hasNextPage
                }
            }
        }
    }

    /**
     * 处理错误响应
     */
    protected open fun handleError(message: String?, exception: Throwable?) {
        if (currentPage == 1) {
            // 首次加载或刷新失败
            _isRefreshing.value = false
            _isLoadingMore.value = false
            _hasMoreData.value = false

            if (_listData.value.isEmpty()) {
                _uiState.value = BaseNetWorkListUiState.Error
            } else {
                _uiState.value = BaseNetWorkListUiState.Success
            }
        } else {
            // 加载更多失败，回退页码
            currentPage--
            _isLoadingMore.value = false
        }
    }

    /**
     * 重试请求
     */
    fun retryRequest() {
        currentPage = 1
        _uiState.value = BaseNetWorkListUiState.Loading
        _isRefreshing.value = false
        _isLoadingMore.value = false
        _hasMoreData.value = false
        loadListData()
    }

    /**
     * 触发下拉刷新
     */
    open fun onRefresh() {
        // 如果正在加载中，则不重复请求
        if (_isLoadingMore.value) {
            return
        }

        refreshStartTime = System.currentTimeMillis()
        _isRefreshing.value = true
        currentPage = 1
        loadListData()
    }

    /**
     * 加载更多数据
     */
    open fun onLoadMore() {
        if (_isLoadingMore.value ||
            !_hasMoreData.value ||
            _listData.value.isEmpty()
        ) {
            return
        }

        loadMoreStartTime = System.currentTimeMillis()
        _isLoadingMore.value = true
        currentPage++
        loadListData()
    }

    /**
     * 设置首次加载成功状态
     */
    private fun setFirstLoadSuccessState(newList: List<T>, hasNextPage: Boolean) {
        _isRefreshing.value = false
        _isLoadingMore.value = false
        _hasMoreData.value = hasNextPage
        _uiState.value = if (newList.isEmpty()) {
            BaseNetWorkListUiState.Empty
        } else {
            BaseNetWorkListUiState.Success
        }
    }

    /**
     * 视图层调用此方法，监听页面刷新信号（基于 NavigationResultKey）。
     *
     * @param key 刷新结果的类型安全 Key，默认使用全局的 [RefreshResultKey]
     *
     * 用法：在 Composable 中调用
     * ```kotlin
     * viewModel.observeRefreshState()
     * ```
     *
     * 只需调用一次，自动去重和解绑，无内存泄漏。
     * 当 [RefreshResult.refresh] 为 true 时触发刷新。
     */
    fun observeRefreshState(
        key: NavigationResultKey<RefreshResult> = RefreshResultKey
    ) {
        if (refreshObserveJob != null) return
        refreshObserveJob = viewModelScope.launch {
            resultEvents(key).collect { refreshResult ->
                if (refreshResult.refresh == true) {
                    onRefresh()
                }
            }
        }
    }
}
