package com.yomo.domain.usecase

import com.yomo.domain.model.request.PlaceCandidateRequestEntity
import com.yomo.domain.repository.VoteRepository
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