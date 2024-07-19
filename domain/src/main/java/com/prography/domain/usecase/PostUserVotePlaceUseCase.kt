package com.prography.domain.usecase

import com.prography.domain.model.request.VotePlaceRequestEntity
import com.prography.domain.repository.VoteRepository

class PostUserVotePlaceUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(
        meetId: Int,
        votePlaceRequestEntity: VotePlaceRequestEntity
    ): Result<Unit> =
        voteRepository.votePlace(meetId, votePlaceRequestEntity)
}