package com.prography.domain.usecase

import android.net.Uri
import com.prography.domain.repository.AuthRepository
import com.skydoves.sandwich.ApiResponse

class PatchUpdateUserImageUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        imageUri: Uri
    ): ApiResponse<Unit> =
        authRepository.updateUserImage(imageUri)
}