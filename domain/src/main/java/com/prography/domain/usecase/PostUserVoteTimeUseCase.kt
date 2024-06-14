package com.prography.domain.usecase

import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.repository.MeetRepository

class PostUserVoteTimeUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(
        userId: Int,
        meetId: Int,
        voteTimeRequestEntity: VoteTimeRequestEntity
    ): Result<Unit> =
        meetRepository.voteTime(userId, meetId, voteTimeRequestEntity)
}