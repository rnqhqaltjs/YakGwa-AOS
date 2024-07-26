package com.prography.data.datasource.remote

import com.prography.data.model.request.RequestConfirmPlaceDto
import com.prography.data.model.request.RequestConfirmTimeDto
import com.prography.data.model.request.RequestPlaceCandidateDto
import com.prography.data.model.request.RequestVotePlaceDto
import com.prography.data.model.request.RequestVoteTimeDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponsePlaceCandidateDto
import com.prography.data.model.response.ResponseTimeCandidateDto
import com.prography.data.model.response.ResponseVotePlaceDto

interface VoteRemoteDataSource {
    suspend fun getPlaceCandidate(meetId: Int): BaseResponse<ResponsePlaceCandidateDto>
    suspend fun getTimeCandidate(meetId: Int): BaseResponse<ResponseTimeCandidateDto>
    suspend fun getVotePlace(meetId: Int): BaseResponse<ResponseVotePlaceDto>

    suspend fun voteTime(
        meetId: Int,
        requestVoteTimeDto: RequestVoteTimeDto
    ): BaseResponse<Unit>

    suspend fun votePlace(
        meetId: Int,
        requestVotePlaceDto: RequestVotePlaceDto
    ): BaseResponse<Unit>

    suspend fun confirmMeetTime(
        meetId: Int,
        requestConfirmTimeDto: RequestConfirmTimeDto
    ): BaseResponse<String>

    suspend fun confirmMeetPlace(
        meetId: Int,
        requestConfirmPlaceDto: RequestConfirmPlaceDto
    ): BaseResponse<String>

    suspend fun addPlaceCandidate(
        meetId: Int,
        requestPlaceCandidateDto: RequestPlaceCandidateDto
    ): BaseResponse<Unit>
}