package com.prography.domain.usecase

import com.prography.domain.model.request.ConfirmPlaceRequestEntity
import com.prography.domain.repository.VoteRepository

class PatchConfirmMeetPlaceUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(
        meetId: Int,
        confirmPlaceRequestEntity: ConfirmPlaceRequestEntity
    ): Result<String> =
        voteRepository.confirmMeetPlace(meetId, confirmPlaceRequestEntity)
}