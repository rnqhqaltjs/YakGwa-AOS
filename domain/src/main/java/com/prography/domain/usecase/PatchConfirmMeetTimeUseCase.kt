package com.prography.domain.usecase

import com.prography.domain.model.request.ConfirmTimeRequestEntity
import com.prography.domain.repository.VoteRepository

class PatchConfirmMeetTimeUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(
        meetId: Int,
        confirmTimeRequestEntity: ConfirmTimeRequestEntity
    ): Result<String> =
        voteRepository.confirmMeetTime(meetId, confirmTimeRequestEntity)
}