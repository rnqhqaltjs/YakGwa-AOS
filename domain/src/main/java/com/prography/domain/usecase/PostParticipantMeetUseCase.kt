package com.prography.domain.usecase

import com.prography.domain.model.response.ParticipantMeetResponseEntity
import com.prography.domain.repository.MeetRepository
import com.skydoves.sandwich.ApiResponse

class PostParticipantMeetUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(meetId: Int): ApiResponse<ParticipantMeetResponseEntity> =
        meetRepository.participantMeet(meetId)
}