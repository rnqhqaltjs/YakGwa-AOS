package com.yomo.domain.usecase

import com.yomo.domain.model.response.MeetsResponseEntity
import com.yomo.domain.repository.MeetRepository
import com.skydoves.sandwich.ApiResponse

class GetParticipantMeetListUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(): ApiResponse<List<MeetsResponseEntity>> =
        meetRepository.getParticipantMeets()
}