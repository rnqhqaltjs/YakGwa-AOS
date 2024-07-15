package com.prography.domain.usecase

import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.repository.VoteRepository

class PostUserVoteTimeUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(
        userId: Int,
        meetId: Int,
        voteTimeRequestEntity: VoteTimeRequestEntity
    ): Result<Unit> =
        voteRepository.voteTime(userId, meetId, voteTimeRequestEntity)
}