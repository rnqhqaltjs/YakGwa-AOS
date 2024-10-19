package com.yomo.domain.usecase

import android.net.Uri
import com.yomo.domain.repository.AuthRepository
import com.skydoves.sandwich.ApiResponse

class PatchUpdateUserImageUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        imageUri: Uri
    ): ApiResponse<Unit> =
        authRepository.updateUserImage(imageUri)
}