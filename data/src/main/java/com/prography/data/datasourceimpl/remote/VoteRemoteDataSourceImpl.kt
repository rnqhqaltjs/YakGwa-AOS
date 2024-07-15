package com.prography.data.datasourceimpl.remote

import com.prography.data.datasource.remote.VoteRemoteDataSource
import com.prography.data.model.request.RequestVotePlaceDto
import com.prography.data.model.request.RequestVoteTimeDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseTimePlaceDto
import com.prography.data.model.response.ResponseVoteInfoDto
import com.prography.data.service.VoteService
import javax.inject.Inject

class VoteRemoteDataSourceImpl @Inject constructor(
    private val voteService: VoteService
) : VoteRemoteDataSource {
    override suspend fun getTimePlaceCandidate(meetId: Int): BaseResponse<ResponseTimePlaceDto> {
        return voteService.getTimePlaceCandidate(meetId)
    }

    override suspend fun voteTime(
        userId: Int,
        meetId: Int,
        requestVoteTimeDto: RequestVoteTimeDto
    ): BaseResponse<Unit> {
        return voteService.voteTime(userId, meetId, requestVoteTimeDto)
    }

    override suspend fun votePlace(
        userId: Int,
        meetId: Int,
        requestVotePlaceDto: RequestVotePlaceDto
    ): BaseResponse<Unit> {
        return voteService.votePlace(userId, meetId, requestVotePlaceDto)
    }

    override suspend fun getVoteInfo(userId: Int, meetId: Int): BaseResponse<ResponseVoteInfoDto> {
        return voteService.getVoteInfo(userId, meetId)
    }
}