package com.yomo.domain.usecase

import com.yomo.domain.model.request.VoteTimeRequestEntity
import com.yomo.domain.repository.VoteRepository
import com.skydoves.sandwich.ApiResponse

class PostUserVoteTimeUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(
        meetId: Int,
        voteTimeRequestEntity: VoteTimeRequestEntity
    ): ApiResponse<Unit> =
        voteRepository.voteTime(meetId, voteTimeRequestEntity)
}