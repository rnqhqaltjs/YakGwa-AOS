package com.prography.domain.usecase

import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.repository.MeetRepository

class GetParticipantMeetListUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(userId: Int): Result<List<MeetsResponseEntity>> =
        meetRepository.getParticipantMeets(userId)
}