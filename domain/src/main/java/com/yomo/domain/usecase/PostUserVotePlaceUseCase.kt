package com.yomo.domain.usecase

import com.yomo.domain.model.request.VotePlaceRequestEntity
import com.yomo.domain.repository.VoteRepository
import com.skydoves.sandwich.ApiResponse

class PostUserVotePlaceUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(
        meetId: Int,
        votePlaceRequestEntity: VotePlaceRequestEntity
    ): ApiResponse<Unit> =
        voteRepository.votePlace(meetId, votePlaceRequestEntity)
}