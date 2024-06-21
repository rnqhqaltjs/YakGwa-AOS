package com.prography.domain.usecase

import com.prography.domain.model.response.TimePlaceResponseEntity
import com.prography.domain.repository.VoteRepository

class GetVoteTimePlaceCandidateInfoUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(meetId: Int): Result<TimePlaceResponseEntity> =
        voteRepository.getTimePlaceCandidate(meetId)
}