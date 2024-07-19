package com.prography.domain.usecase

import com.prography.domain.model.response.TimeCandidateResponseEntity
import com.prography.domain.repository.VoteRepository

class GetVoteTimeCandidateInfoUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(meetId: Int): Result<TimeCandidateResponseEntity> =
        voteRepository.getTimeCandidate(meetId)
}