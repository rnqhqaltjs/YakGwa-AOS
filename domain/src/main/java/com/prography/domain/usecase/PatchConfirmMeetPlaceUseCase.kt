package com.prography.domain.usecase

import com.prography.domain.model.request.ConfirmPlaceRequestEntity
import com.prography.domain.repository.VoteRepository
import com.skydoves.sandwich.ApiResponse

class PatchConfirmMeetPlaceUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(
        meetId: Int,
        confirmPlaceRequestEntity: ConfirmPlaceRequestEntity
    ): ApiResponse<String> =
        voteRepository.confirmMeetPlace(meetId, confirmPlaceRequestEntity)
}