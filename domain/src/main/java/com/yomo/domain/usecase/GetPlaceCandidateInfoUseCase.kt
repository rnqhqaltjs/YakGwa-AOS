package com.yomo.domain.usecase

import com.yomo.domain.model.response.PlaceCandidateResponseEntity
import com.yomo.domain.repository.VoteRepository
import com.skydoves.sandwich.ApiResponse

class GetPlaceCandidateInfoUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(meetId: Int): ApiResponse<List<PlaceCandidateResponseEntity>> =
        voteRepository.getPlaceCandidate(meetId)
}