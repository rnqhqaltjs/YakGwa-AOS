package com.prography.domain.usecase

import com.prography.domain.model.response.PlaceCandidateResponseEntity
import com.prography.domain.repository.VoteRepository

class GetPlaceCandidateInfoUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(meetId: Int): Result<List<PlaceCandidateResponseEntity>> =
        voteRepository.getPlaceCandidate(meetId)
}