package com.prography.domain.usecase

import com.prography.domain.model.response.ThemesResponseEntity
import com.prography.domain.repository.MeetRepository
import com.skydoves.sandwich.ApiResponse

class GetThemeListUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(): ApiResponse<List<ThemesResponseEntity>> =
        meetRepository.getMeetThemes()
}