package com.yomo.data.datasourceimpl.remote

import com.yomo.data.datasource.remote.VoteRemoteDataSource
import com.yomo.data.model.request.RequestConfirmPlaceDto
import com.yomo.data.model.request.RequestConfirmTimeDto
import com.yomo.data.model.request.RequestPlaceCandidateDto
import com.yomo.data.model.request.RequestVotePlaceDto
import com.yomo.data.model.request.RequestVoteTimeDto
import com.yomo.data.model.response.BaseResponse
import com.yomo.data.model.response.ResponsePlaceCandidateDto
import com.yomo.data.model.response.ResponseTimeCandidateDto
import com.yomo.data.model.response.ResponseVotePlaceDto
import com.yomo.data.remote.VoteService
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class VoteRemoteDataSourceImpl @Inject constructor(
    private val voteService: VoteService
) : VoteRemoteDataSource {
    override suspend fun getPlaceCandidate(meetId: Int): ApiResponse<BaseResponse<ResponsePlaceCandidateDto>> {
        return voteService.getPlaceCandidate(meetId)
    }

    override suspend fun getTimeCandidate(meetId: Int): ApiResponse<BaseResponse<ResponseTimeCandidateDto>> {
        return voteService.getTimeCandidate(meetId)
    }

    override suspend fun voteTime(
        meetId: Int,
        requestVoteTimeDto: RequestVoteTimeDto
    ): ApiResponse<Unit> {
        return voteService.voteTime(meetId, requestVoteTimeDto)
    }

    override suspend fun votePlace(
        meetId: Int,
        requestVotePlaceDto: RequestVotePlaceDto
    ): ApiResponse<Unit> {
        return voteService.votePlace(meetId, requestVotePlaceDto)
    }

    override suspend fun confirmMeetTime(
        meetId: Int,
        requestConfirmTimeDto: RequestConfirmTimeDto
    ): ApiResponse<BaseResponse<String>> {
        return voteService.confirmMeetTime(meetId, requestConfirmTimeDto)
    }

    override suspend fun confirmMeetPlace(
        meetId: Int,
        requestConfirmPlaceDto: RequestConfirmPlaceDto
    ): ApiResponse<BaseResponse<String>> {
        return voteService.confirmMeetPlace(meetId, requestConfirmPlaceDto)
    }

    override suspend fun addPlaceCandidate(
        meetId: Int,
        requestPlaceCandidateDto: RequestPlaceCandidateDto
    ): ApiResponse<Unit> {
        return voteService.addPlaceCandidate(meetId, requestPlaceCandidateDto)
    }

    override suspend fun getVotePlace(meetId: Int): ApiResponse<BaseResponse<ResponseVotePlaceDto>> {
        return voteService.getVotePlace(meetId)
    }
}