package com.yomo.data.datasource.remote

import com.yomo.data.model.request.RequestAuthDto
import com.yomo.data.model.response.BaseResponse
import com.yomo.data.model.response.ResponseAuthDto
import com.yomo.data.model.response.ResponseUserInfoDto
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody

interface AuthRemoteDataSource {
    suspend fun login(
        kakaoAccessToken: String,
        requestAuthDto: RequestAuthDto
    ): ApiResponse<BaseResponse<ResponseAuthDto>>

    suspend fun logout(): ApiResponse<Unit>
    suspend fun getUserInfo(): ApiResponse<BaseResponse<ResponseUserInfoDto>>
    suspend fun updateUserImage(userImage: MultipartBody.Part): ApiResponse<Unit>
    suspend fun updateFcmToken(newFcmToken: String): ApiResponse<Unit>
    suspend fun signout(): ApiResponse<Unit>
}