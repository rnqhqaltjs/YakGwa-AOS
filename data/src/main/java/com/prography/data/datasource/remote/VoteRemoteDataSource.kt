package com.prography.data.datasource.remote

import com.prography.data.model.request.RequestVotePlaceDto
import com.prography.data.model.request.RequestVoteTimeDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponsePlaceCandidateDto
import com.prography.data.model.response.ResponseTimeCandidateDto
import com.prography.data.model.response.ResponseVotePlaceDto

interface VoteRemoteDataSource {
    suspend fun getPlaceCandidate(meetId: Int): BaseResponse<ResponsePlaceCandidateDto>
    suspend fun getTimeCandidate(meetId: Int): BaseResponse<ResponseTimeCandidateDto>

    suspend fun voteTime(
        meetId: Int,
        requestVoteTimeDto: RequestVoteTimeDto
    ): BaseResponse<Unit>

    suspend fun votePlace(
        meetId: Int,
        requestVotePlaceDto: RequestVotePlaceDto
    ): BaseResponse<Unit>

    suspend fun getVotePlace(
        meetId: Int
    ): BaseResponse<ResponseVotePlaceDto>
}