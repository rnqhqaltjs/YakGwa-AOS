package com.prography.data.datasource.remote

import com.prography.data.model.request.RequestAuthDto
import com.prography.data.model.response.AuthResponse.ResponseAuthDto
import com.prography.data.model.response.BaseResponse

interface AuthRemoteDataSource {
    suspend fun login(
        kakaoAccessToken: String,
        requestAuthDto: RequestAuthDto
    ): BaseResponse<ResponseAuthDto>

    suspend fun logout(accessToken: String): BaseResponse<Unit>
}