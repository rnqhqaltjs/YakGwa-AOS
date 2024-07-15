package com.prography.data.service

import com.prography.data.model.request.RequestVotePlaceDto
import com.prography.data.model.request.RequestVoteTimeDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseTimePlaceDto
import com.prography.data.model.response.ResponseVoteInfoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VoteService {
    @GET("/api/v1/meets/{meetId}/vote/items")
    suspend fun getTimePlaceCandidate(@Path("meetId") meetId: Int): BaseResponse<ResponseTimePlaceDto>

    @POST("/api/v1/users/{userId}/meets/{meetId}/vote/schedules")
    suspend fun voteTime(
        @Path("userId") userId: Int,
        @Path("meetId") meetId: Int,
        @Body requestVoteTimeDto: RequestVoteTimeDto
    ): BaseResponse<Unit>

    @POST("/api/v1/users/{userId}/meets/{meetId}/vote/places")
    suspend fun votePlace(
        @Path("userId") userId: Int,
        @Path("meetId") meetId: Int,
        @Body requestVoteTimeDto: RequestVotePlaceDto
    ): BaseResponse<Unit>

    @GET("/api/v1/users/{userId}/meets/{meetId}/vote")
    suspend fun getVoteInfo(
        @Path("userId") userId: Int,
        @Path("meetId") meetId: Int
    ): BaseResponse<ResponseVoteInfoDto>
}