package com.prography.domain.repository

import com.prography.domain.model.request.AuthRequestEntity
import com.prography.domain.model.response.AuthResponseEntity

interface AuthRepository {
    suspend fun postLogin(
        kakaoAccessToken: String,
        authRequestEntity: AuthRequestEntity
    ): Result<AuthResponseEntity>

    suspend fun logout(accessToken: String): Result<Unit>
}