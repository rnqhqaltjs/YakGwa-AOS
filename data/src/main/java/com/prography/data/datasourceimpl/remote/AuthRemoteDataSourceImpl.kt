package com.prography.data.datasourceimpl.remote

import com.prography.data.datasource.remote.AuthRemoteDataSource
import com.prography.data.model.request.RequestAuthDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseAuthDto
import com.prography.data.model.response.ResponseUserInfoDto
import com.prography.data.remote.AuthService
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
}