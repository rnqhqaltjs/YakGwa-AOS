package com.prography.data.service

import com.prography.data.model.request.RequestAuthDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseAuthDto
import com.prography.data.model.response.ResponseUserInfoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("/api/v1/auth/login")
    suspend fun postLogin(
        @Header("Authorization") header: String,
        @Body requestAuthDto: RequestAuthDto
    ): BaseResponse<ResponseAuthDto>

    @POST("/api/v1/auth/logout")
    suspend fun logout(): BaseResponse<Unit>

    @GET("/api/v1/user")
    suspend fun getUserInfo(): BaseResponse<ResponseUserInfoDto>
}