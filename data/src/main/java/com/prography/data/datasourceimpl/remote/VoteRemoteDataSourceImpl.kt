package com.prography.data.datasourceimpl.remote

import com.prography.data.datasource.remote.VoteRemoteDataSource
import com.prography.data.model.request.RequestConfirmPlaceDto
import com.prography.data.model.request.RequestConfirmTimeDto
import com.prography.data.model.request.RequestPlaceCandidateDto
import com.prography.data.model.request.RequestVotePlaceDto
import com.prography.data.model.request.RequestVoteTimeDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponsePlaceCandidateDto
import com.prography.data.model.response.ResponseTimeCandidateDto
import com.prography.data.model.response.ResponseVotePlaceDto
import com.prography.data.remote.VoteService
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