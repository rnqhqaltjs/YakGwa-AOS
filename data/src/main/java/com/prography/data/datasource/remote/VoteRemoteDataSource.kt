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
import com.skydoves.sandwich.ApiResponse

interface VoteRemoteDataSource {
    suspend fun getPlaceCandidate(meetId: Int): ApiResponse<BaseResponse<ResponsePlaceCandidateDto>>
    suspend fun getTimeCandidate(meetId: Int): ApiResponse<BaseResponse<ResponseTimeCandidateDto>>
    suspend fun getVotePlace(meetId: Int): ApiResponse<BaseResponse<ResponseVotePlaceDto>>

    suspend fun voteTime(
        meetId: Int,
        requestVoteTimeDto: RequestVoteTimeDto
    ): ApiResponse<Unit>

    suspend fun votePlace(
        meetId: Int,
        requestVotePlaceDto: RequestVotePlaceDto
    ): ApiResponse<Unit>

    suspend fun confirmMeetTime(
        meetId: Int,
        requestConfirmTimeDto: RequestConfirmTimeDto
    ): ApiResponse<BaseResponse<String>>

    suspend fun confirmMeetPlace(
        meetId: Int,
        requestConfirmPlaceDto: RequestConfirmPlaceDto
    ): ApiResponse<BaseResponse<String>>

    suspend fun addPlaceCandidate(
        meetId: Int,
        requestPlaceCandidateDto: RequestPlaceCandidateDto
    ): ApiResponse<Unit>
}