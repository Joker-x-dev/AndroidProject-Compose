package com.joker.kit.core.data.repository

import com.joker.kit.core.model.entity.User
import com.joker.kit.core.model.network.NetworkResponse
import com.joker.kit.core.network.datasource.userinfo.UserInfoNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * 用户信息相关仓库
 *
 * @param userInfoNetworkDataSource 用户信息网络数据源
 * @author Joker.X
 */
class UserInfoRepository @Inject constructor(
    private val userInfoNetworkDataSource: UserInfoNetworkDataSource
) {
    /**
     * 获取用户个人信息
     *
     * @return 用户信息Flow
     * @author Joker.X
     */
    fun getPersonInfo(): Flow<NetworkResponse<User>> = flow {
        emit(userInfoNetworkDataSource.getPersonInfo())
    }.flowOn(Dispatchers.IO)
}
