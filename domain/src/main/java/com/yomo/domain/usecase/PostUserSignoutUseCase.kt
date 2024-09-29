package com.yomo.domain.usecase

import com.yomo.domain.repository.AuthRepository
import com.skydoves.sandwich.ApiResponse

class PostUserSignoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): ApiResponse<Unit> = authRepository.signout()
}