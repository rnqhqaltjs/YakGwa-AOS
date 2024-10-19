package com.yomo.domain.usecase

import com.yomo.domain.model.request.ConfirmPlaceRequestEntity
import com.yomo.domain.repository.VoteRepository
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