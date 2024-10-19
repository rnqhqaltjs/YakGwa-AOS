package com.yomo.domain.usecase

import com.yomo.domain.model.request.ConfirmTimeRequestEntity
import com.yomo.domain.repository.VoteRepository
import com.skydoves.sandwich.ApiResponse

class PatchConfirmMeetTimeUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(
        meetId: Int,
        confirmTimeRequestEntity: ConfirmTimeRequestEntity
    ): ApiResponse<String> =
        voteRepository.confirmMeetTime(meetId, confirmTimeRequestEntity)
}