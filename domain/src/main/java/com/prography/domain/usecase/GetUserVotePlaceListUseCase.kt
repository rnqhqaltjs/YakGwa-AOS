package com.prography.domain.usecase

import com.prography.domain.model.response.VotePlaceResponseEntity
import com.prography.domain.repository.VoteRepository

class GetUserVotePlaceListUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(meetId: Int): Result<VotePlaceResponseEntity> =
        voteRepository.getVotePlace(meetId)
}