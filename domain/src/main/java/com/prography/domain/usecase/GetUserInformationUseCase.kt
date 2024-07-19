package com.prography.domain.usecase

import com.prography.domain.model.response.UserInfoResponseEntity
import com.prography.domain.repository.AuthRepository

class GetUserInformationUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<UserInfoResponseEntity> =
        authRepository.getUserInfo()
}