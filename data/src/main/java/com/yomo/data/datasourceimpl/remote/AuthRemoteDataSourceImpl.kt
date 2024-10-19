package com.yomo.data.datasourceimpl.remote

import com.yomo.data.datasource.remote.AuthRemoteDataSource
import com.yomo.data.model.request.RequestAuthDto
import com.yomo.data.model.response.BaseResponse
import com.yomo.data.model.response.ResponseAuthDto
import com.yomo.data.model.response.ResponseUserInfoDto
import com.yomo.data.remote.AuthService
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthRemoteDataSource {
    override suspend fun login(
        kakaoAccessToken: String,
        requestAuthDto: RequestAuthDto
    ): ApiResponse<BaseResponse<ResponseAuthDto>> {
        return authService.postLogin(kakaoAccessToken, requestAuthDto)
    }

    override suspend fun logout(): ApiResponse<Unit> {
        return authService.logout()
    }

    override suspend fun getUserInfo(): ApiResponse<BaseResponse<ResponseUserInfoDto>> {
        return authService.getUserInfo()
    }

    override suspend fun updateUserImage(userImage: MultipartBody.Part): ApiResponse<Unit> {
        return authService.updateUserImage(userImage)
    }

    override suspend fun updateFcmToken(newFcmToken: String): ApiResponse<Unit> {
        return authService.updateFcmToken(newFcmToken)
    }

    override suspend fun signout(): ApiResponse<Unit> {
        return authService.signout()
    }
}