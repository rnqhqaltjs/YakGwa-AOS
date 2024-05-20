package com.prography.data.service

import com.prography.data.model.request.RequestAuthDto
import com.prography.data.model.response.AuthResponse.ResponseAuthDto
import com.prography.data.model.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("/api/v1/auth/login")
    suspend fun postLogin(
        @Header("Authorization") header: String,
        @Body requestAuthDto: RequestAuthDto
    ): BaseResponse<ResponseAuthDto>

    @POST("/api/v1/auth/logout")
    suspend fun logout(@Header("Authorization") header: String): BaseResponse<Unit>
}