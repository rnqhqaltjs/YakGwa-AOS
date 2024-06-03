package com.prography.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface YakGwaLocalDataSource {
    val accessToken: Flow<String>
    val refreshToken: Flow<String>
    val userId: Flow<Int>
    val isLogin: Flow<Boolean>

    suspend fun saveAccessToken(accessToken: String)
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun saveUserId(userId: Int)
    suspend fun saveIsLogin(isLogin: Boolean)
    suspend fun clear()
}