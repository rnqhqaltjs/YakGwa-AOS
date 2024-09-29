package com.yomo.domain.usecase

import com.yomo.domain.model.request.AuthRequestEntity
import com.yomo.domain.model.response.AuthResponseEntity
import com.yomo.domain.repository.AuthRepository
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