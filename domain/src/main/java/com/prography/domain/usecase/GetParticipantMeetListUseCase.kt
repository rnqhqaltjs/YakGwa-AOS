package com.prography.domain.usecase

import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.repository.MeetRepository
import com.skydoves.sandwich.ApiResponse

class GetParticipantMeetListUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(): ApiResponse<List<MeetsResponseEntity>> =
        meetRepository.getParticipantMeets()
}