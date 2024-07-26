package com.prography.data.datasource.remote

import com.prography.data.model.request.RequestAuthDto
import com.prography.data.model.request.RequestUserImageDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseAuthDto
import com.prography.data.model.response.ResponseUserInfoDto

interface AuthRemoteDataSource {
    suspend fun login(
        kakaoAccessToken: String,
        requestAuthDto: RequestAuthDto
    ): BaseResponse<ResponseAuthDto>

    suspend fun logout(): BaseResponse<Unit>
    suspend fun getUserInfo(): BaseResponse<ResponseUserInfoDto>
    suspend fun updateUserImage(requestUserImageDto: RequestUserImageDto): BaseResponse<Unit>
}