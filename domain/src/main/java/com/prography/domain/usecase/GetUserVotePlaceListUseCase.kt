package com.prography.domain.usecase

import com.prography.domain.model.response.VotePlaceResponseEntity
import com.prography.domain.repository.VoteRepository
import com.skydoves.sandwich.ApiResponse

class GetUserVotePlaceListUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(meetId: Int): ApiResponse<VotePlaceResponseEntity> =
        voteRepository.getVotePlace(meetId)
}