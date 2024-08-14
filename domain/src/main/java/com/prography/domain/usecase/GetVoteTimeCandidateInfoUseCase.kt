package com.prography.domain.usecase

import com.prography.domain.model.response.TimeCandidateResponseEntity
import com.prography.domain.repository.VoteRepository
import com.skydoves.sandwich.ApiResponse

class GetVoteTimeCandidateInfoUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(meetId: Int): ApiResponse<TimeCandidateResponseEntity> =
        voteRepository.getTimeCandidate(meetId)
}