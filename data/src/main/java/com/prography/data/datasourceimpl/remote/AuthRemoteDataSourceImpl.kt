package com.prography.data.datasourceimpl.remote

import com.prography.data.datasource.remote.AuthRemoteDataSource
import com.prography.data.model.request.RequestAuthDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseAuthDto
import com.prography.data.model.response.ResponseUserInfoDto
import com.prography.data.service.AuthService
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthRemoteDataSource {
    override suspend fun login(
        kakaoAccessToken: String,
        requestAuthDto: RequestAuthDto
    ): BaseResponse<ResponseAuthDto> {
        return authService.postLogin(kakaoAccessToken, requestAuthDto)
    }

    override suspend fun logout(): BaseResponse<Unit> {
        return authService.logout()
    }

    override suspend fun getUserInfo(): BaseResponse<ResponseUserInfoDto> {
        return authService.getUserInfo()
    }
}