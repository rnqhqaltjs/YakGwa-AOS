package com.prography.domain.usecase

import com.prography.domain.repository.MeetRepository

class PostMeetParticipantUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(userId: Int, meetId: Int): Result<Unit> =
        meetRepository.participantMeet(userId, meetId)
}