package com.prography.domain.usecase

import com.prography.domain.model.request.UserImageRequestEntity
import com.prography.domain.repository.AuthRepository

class PatchUpdateUserImageUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        userImageRequestEntity: UserImageRequestEntity
    ): Result<Unit> =
        authRepository.updateUserImage(userImageRequestEntity)
}