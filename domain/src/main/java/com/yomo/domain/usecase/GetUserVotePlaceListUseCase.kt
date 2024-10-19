package com.yomo.domain.usecase

import com.yomo.domain.model.response.VotePlaceResponseEntity
import com.yomo.domain.repository.VoteRepository
import com.skydoves.sandwich.ApiResponse

class GetUserVotePlaceListUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(meetId: Int): ApiResponse<VotePlaceResponseEntity> =
        voteRepository.getVotePlace(meetId)
}