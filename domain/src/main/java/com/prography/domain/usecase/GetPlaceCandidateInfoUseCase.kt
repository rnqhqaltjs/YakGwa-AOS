package com.prography.domain.usecase

import com.prography.domain.model.response.PlaceCandidateResponseEntity
import com.prography.domain.repository.VoteRepository
import com.skydoves.sandwich.ApiResponse

class GetPlaceCandidateInfoUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(meetId: Int): ApiResponse<List<PlaceCandidateResponseEntity>> =
        voteRepository.getPlaceCandidate(meetId)
}