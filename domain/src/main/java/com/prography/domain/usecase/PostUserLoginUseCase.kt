package com.prography.domain.usecase

import com.prography.domain.model.request.AuthRequestEntity
import com.prography.domain.model.response.AuthResponseEntity
import com.prography.domain.repository.AuthRepository
import com.skydoves.sandwich.ApiResponse

class PostUserLoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        kakaoAccessToken: String,
        authRequestEntity: AuthRequestEntity
    ): ApiResponse<AuthResponseEntity> =
        authRepository.postLogin(kakaoAccessToken, authRequestEntity)
}