package com.prography.domain.usecase

import com.prography.domain.model.response.ThemesResponseEntity
import com.prography.domain.repository.MeetRepository

class GetThemeListUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(): Result<List<ThemesResponseEntity>> =
        meetRepository.getMeetThemes()
}