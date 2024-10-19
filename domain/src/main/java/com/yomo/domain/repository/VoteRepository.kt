package com.yomo.domain.repository

import com.yomo.domain.model.request.ConfirmPlaceRequestEntity
import com.yomo.domain.model.request.ConfirmTimeRequestEntity
import com.yomo.domain.model.request.PlaceCandidateRequestEntity
import com.yomo.domain.model.request.VotePlaceRequestEntity
import com.yomo.domain.model.request.VoteTimeRequestEntity
import com.yomo.domain.model.response.PlaceCandidateResponseEntity
import com.yomo.domain.model.response.TimeCandidateResponseEntity
import com.yomo.domain.model.response.VotePlaceResponseEntity
import com.skydoves.sandwich.ApiResponse

interface VoteRepository {
    suspend fun getPlaceCandidate(meetId: Int): ApiResponse<List<PlaceCandidateResponseEntity>>
    suspend fun getTimeCandidate(meetId: Int): ApiResponse<TimeCandidateResponseEntity>
    suspend fun getVotePlace(meetId: Int): ApiResponse<VotePlaceResponseEntity>

    suspend fun voteTime(
        meetId: Int,
        voteTimeRequestEntity: VoteTimeRequestEntity
    ): ApiResponse<Unit>

    suspend fun votePlace(
        meetId: Int,
        votePlaceRequestEntity: VotePlaceRequestEntity
    ): ApiResponse<Unit>

    suspend fun confirmMeetTime(
        meetId: Int,
        confirmTimeRequestEntity: ConfirmTimeRequestEntity
    ): ApiResponse<String>

    suspend fun confirmMeetPlace(
        meetId: Int,
        confirmPlaceRequestEntity: ConfirmPlaceRequestEntity
    ): ApiResponse<String>

    suspend fun addPlaceCandidate(
        meetId: Int,
        placeCandidateRequestEntity: PlaceCandidateRequestEntity
    ): ApiResponse<Unit>
}