package com.prography.domain.usecase

import com.prography.domain.model.response.TimePlaceResponseEntity
import com.prography.domain.repository.MeetRepository

class GetVoteTimePlaceCandidateInfoUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(meetId: Int): Result<TimePlaceResponseEntity> =
        meetRepository.getTimePlaceCandidate(meetId)
}