package com.prography.domain.usecase

import com.prography.domain.model.response.VoteInfoResponseEntity
import com.prography.domain.repository.VoteRepository

class GetUserVoteInfoUsecase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(
        userId: Int,
        meetId: Int,
    ): Result<VoteInfoResponseEntity> =
        voteRepository.getVoteInfo(userId, meetId)
}