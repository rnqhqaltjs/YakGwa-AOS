package com.prography.domain.repository

import android.net.Uri
import com.prography.domain.model.request.AuthRequestEntity
import com.prography.domain.model.response.AuthResponseEntity
import com.prography.domain.model.response.UserInfoResponseEntity

interface AuthRepository {
    suspend fun postLogin(
        kakaoAccessToken: String,
        authRequestEntity: AuthRequestEntity
    ): Result<AuthResponseEntity>

    suspend fun logout(): Result<Unit>
    suspend fun getUserInfo(): Result<UserInfoResponseEntity>
    suspend fun updateUserImage(imageUri: Uri): Result<Unit>
}