package com.yomo.domain.usecase

import com.yomo.domain.model.response.ParticipantMeetResponseEntity
import com.yomo.domain.repository.MeetRepository
import com.skydoves.sandwich.ApiResponse

class PostParticipantMeetUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(meetId: Int): ApiResponse<ParticipantMeetResponseEntity> =
        meetRepository.participantMeet(meetId)
}