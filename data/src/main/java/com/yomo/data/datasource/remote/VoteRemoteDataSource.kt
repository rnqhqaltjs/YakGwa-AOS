package com.yomo.data.datasource.remote

import com.yomo.data.model.request.RequestConfirmPlaceDto
import com.yomo.data.model.request.RequestConfirmTimeDto
import com.yomo.data.model.request.RequestPlaceCandidateDto
import com.yomo.data.model.request.RequestVotePlaceDto
import com.yomo.data.model.request.RequestVoteTimeDto
import com.yomo.data.model.response.BaseResponse
import com.yomo.data.model.response.ResponsePlaceCandidateDto
import com.yomo.data.model.response.ResponseTimeCandidateDto
import com.yomo.data.model.response.ResponseVotePlaceDto
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