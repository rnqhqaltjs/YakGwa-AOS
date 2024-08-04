package com.prography.domain.usecase

import android.net.Uri
import com.prography.domain.repository.AuthRepository

class PatchUpdateUserImageUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        imageUri: Uri
    ): Result<Unit> =
        authRepository.updateUserImage(imageUri)
}