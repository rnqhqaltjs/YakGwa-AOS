package com.prography.domain.usecase

import com.prography.domain.model.response.UserInfoResponseEntity
import com.prography.domain.repository.AuthRepository
import com.skydoves.sandwich.ApiResponse

class GetUserInformationUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): ApiResponse<UserInfoResponseEntity> =
        authRepository.getUserInfo()
}