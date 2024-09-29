package com.yomo.domain.usecase

import com.yomo.domain.model.response.UserInfoResponseEntity
import com.yomo.domain.repository.AuthRepository
import com.skydoves.sandwich.ApiResponse

class GetUserInformationUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): ApiResponse<UserInfoResponseEntity> =
        authRepository.getUserInfo()
}