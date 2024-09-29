package com.yomo.domain.usecase

import com.yomo.domain.model.response.TimeCandidateResponseEntity
import com.yomo.domain.repository.VoteRepository
import com.skydoves.sandwich.ApiResponse

class GetVoteTimeCandidateInfoUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(meetId: Int): ApiResponse<TimeCandidateResponseEntity> =
        voteRepository.getTimeCandidate(meetId)
}