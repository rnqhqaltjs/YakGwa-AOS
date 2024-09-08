package com.prography.data.remote

import com.prography.data.model.request.RequestAuthDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseAuthDto
import com.prography.data.model.response.ResponseUserInfoDto
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AuthService {
    @POST("/api/v1/auth/login")
    suspend fun postLogin(
        @Header("Authorization") header: String,
        @Body requestAuthDto: RequestAuthDto
    ): ApiResponse<BaseResponse<ResponseAuthDto>>

    @POST("/api/v1/auth/logout")
    suspend fun logout(): ApiResponse<Unit>

    @GET("/api/v1/user")
    suspend fun getUserInfo(): ApiResponse<BaseResponse<ResponseUserInfoDto>>

    @Multipart
    @PATCH("/api/v1/user/image")
    suspend fun updateUserImage(@Part userImage: MultipartBody.Part): ApiResponse<Unit>

    @PATCH("/api/v1/user/fcm")
    suspend fun updateFcmToken(@Query("newFcmToken") newFcmToken: String): ApiResponse<Unit>

    @POST("/api/v1/auth/signout")
    suspend fun signout(): ApiResponse<Unit>
}