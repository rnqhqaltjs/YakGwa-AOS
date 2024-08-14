package com.prography.domain.usecase

import com.prography.domain.model.request.PlaceCandidateRequestEntity
import com.prography.domain.repository.VoteRepository
import com.skydoves.sandwich.ApiResponse

class PostPlaceCandidateInfoUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(
        meetId: Int,
        placeCandidateRequestEntity: PlaceCandidateRequestEntity
    ): ApiResponse<Unit> =
        voteRepository.addPlaceCandidate(meetId, placeCandidateRequestEntity)
}