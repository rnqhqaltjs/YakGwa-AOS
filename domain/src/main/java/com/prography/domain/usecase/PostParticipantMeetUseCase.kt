package com.prography.domain.usecase

import com.prography.domain.model.response.ParticipantMeetResponseEntity
import com.prography.domain.repository.MeetRepository

class PostParticipantMeetUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(meetId: Int): Result<ParticipantMeetResponseEntity> =
        meetRepository.participantMeet(meetId)
}