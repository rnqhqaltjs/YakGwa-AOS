package com.prography.domain.repository

import android.net.Uri
import com.prography.domain.model.request.AuthRequestEntity
import com.prography.domain.model.response.AuthResponseEntity
import com.prography.domain.model.response.UserInfoResponseEntity
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
}