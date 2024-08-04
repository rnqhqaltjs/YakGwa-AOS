package com.prography.data.remote

import com.prography.data.model.request.RequestAuthDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseAuthDto
import com.prography.data.model.response.ResponseUserInfoDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

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

    @Multipart
    @PATCH("/api/v1/user/image")
    suspend fun updateUserImage(@Part userImage: MultipartBody.Part): BaseResponse<Unit>
}