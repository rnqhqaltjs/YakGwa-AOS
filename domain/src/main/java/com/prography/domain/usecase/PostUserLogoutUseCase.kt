package com.prography.domain.usecase

import com.prography.domain.repository.AuthRepository
import com.skydoves.sandwich.ApiResponse

class PostUserLogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): ApiResponse<Unit> = authRepository.logout()
}