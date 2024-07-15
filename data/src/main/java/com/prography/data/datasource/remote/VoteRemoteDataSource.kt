package com.prography.data.datasource.remote

import com.prography.data.model.request.RequestVotePlaceDto
import com.prography.data.model.request.RequestVoteTimeDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseTimePlaceDto
import com.prography.data.model.response.ResponseVoteInfoDto

interface VoteRemoteDataSource {
    suspend fun getTimePlaceCandidate(meetId: Int): BaseResponse<ResponseTimePlaceDto>

    suspend fun voteTime(
        userId: Int,
        meetId: Int,
        requestVoteTimeDto: RequestVoteTimeDto
    ): BaseResponse<Unit>

    suspend fun votePlace(
        userId: Int,
        meetId: Int,
        requestVotePlaceDto: RequestVotePlaceDto
    ): BaseResponse<Unit>

    suspend fun getVoteInfo(
        userId: Int,
        meetId: Int
    ): BaseResponse<ResponseVoteInfoDto>
}