package com.prography.domain.repository

import com.prography.domain.model.request.ConfirmPlaceRequestEntity
import com.prography.domain.model.request.ConfirmTimeRequestEntity
import com.prography.domain.model.request.PlaceCandidateRequestEntity
import com.prography.domain.model.request.VotePlaceRequestEntity
import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.model.response.PlaceCandidateResponseEntity
import com.prography.domain.model.response.TimeCandidateResponseEntity
import com.prography.domain.model.response.VotePlaceResponseEntity

interface VoteRepository {
    suspend fun getPlaceCandidate(meetId: Int): Result<List<PlaceCandidateResponseEntity>>
    suspend fun getTimeCandidate(meetId: Int): Result<TimeCandidateResponseEntity>
    suspend fun getVotePlace(meetId: Int): Result<VotePlaceResponseEntity>

    suspend fun voteTime(
        meetId: Int,
        voteTimeRequestEntity: VoteTimeRequestEntity
    ): Result<Unit>

    suspend fun votePlace(
        meetId: Int,
        votePlaceRequestEntity: VotePlaceRequestEntity
    ): Result<Unit>

    suspend fun confirmMeetTime(
        meetId: Int,
        confirmTimeRequestEntity: ConfirmTimeRequestEntity
    ): Result<String>

    suspend fun confirmMeetPlace(
        meetId: Int,
        confirmPlaceRequestEntity: ConfirmPlaceRequestEntity
    ): Result<String>

    suspend fun addPlaceCandidate(
        meetId: Int,
        placeCandidateRequestEntity: PlaceCandidateRequestEntity
    ): Result<Unit>
}