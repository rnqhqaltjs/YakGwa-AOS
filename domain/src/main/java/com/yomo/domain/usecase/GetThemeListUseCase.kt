package com.yomo.domain.usecase

import com.yomo.domain.model.response.ThemesResponseEntity
import com.yomo.domain.repository.MeetRepository
import com.skydoves.sandwich.ApiResponse

class GetThemeListUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(): ApiResponse<List<ThemesResponseEntity>> =
        meetRepository.getMeetThemes()
}