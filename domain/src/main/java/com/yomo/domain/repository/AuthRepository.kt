package com.yomo.domain.repository

import android.net.Uri
import com.yomo.domain.model.request.AuthRequestEntity
import com.yomo.domain.model.response.AuthResponseEntity
import com.yomo.domain.model.response.UserInfoResponseEntity
import com.skydoves.sandwich.ApiResponse

interface AuthRepository {
    suspend fun postLogin(
        kakaoAccessToken: String,
        authRequestEntity: AuthRequestEntity
    ): ApiResponse<AuthResponseEntity>

    suspend fun logout(): ApiResponse<Unit>
    suspend fun getUserInfo(): ApiResponse<UserInfoResponseEntity>
    suspend fun updateUserImage(imageUri: Uri): ApiResponse<Unit>
    suspend fun updateFcmToken(newFcmToken: String): ApiResponse<Unit>
    suspend fun signout(): ApiResponse<Unit>
}