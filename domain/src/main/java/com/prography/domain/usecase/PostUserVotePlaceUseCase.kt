package com.prography.domain.usecase

import com.prography.domain.model.request.VotePlaceRequestEntity
import com.prography.domain.repository.MeetRepository

class PostUserVotePlaceUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(
        userId: Int,
        meetId: Int,
        votePlaceRequestEntity: VotePlaceRequestEntity
    ): Result<Unit> =
        meetRepository.votePlace(userId, meetId, votePlaceRequestEntity)
}