package com.prography.domain.usecase

import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.repository.VoteRepository

class PostUserVoteTimeUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(
        meetId: Int,
        voteTimeRequestEntity: VoteTimeRequestEntity
    ): Result<Unit> =
        voteRepository.voteTime(meetId, voteTimeRequestEntity)
}