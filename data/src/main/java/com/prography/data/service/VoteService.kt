package com.prography.data.service

import com.prography.data.model.request.RequestConfirmPlaceDto
import com.prography.data.model.request.RequestConfirmTimeDto
import com.prography.data.model.request.RequestVotePlaceDto
import com.prography.data.model.request.RequestVoteTimeDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponsePlaceCandidateDto
import com.prography.data.model.response.ResponseTimeCandidateDto
import com.prography.data.model.response.ResponseVotePlaceDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface VoteService {
    @GET("/api/v1/meets/{meetId}/placeslots")
    suspend fun getPlaceCandidate(@Path("meetId") meetId: Int): BaseResponse<ResponsePlaceCandidateDto>

    @GET("/api/v1/meets/{meetId}/times")
    suspend fun getTimeCandidate(@Path("meetId") meetId: Int): BaseResponse<ResponseTimeCandidateDto>

    @POST("/api/v1/meets/{meetId}/times")
    suspend fun voteTime(
        @Path("meetId") meetId: Int,
        @Body requestVoteTimeDto: RequestVoteTimeDto
    ): BaseResponse<Unit>

    @POST("/api/v1/meets/{meetId}/places")
    suspend fun votePlace(
        @Path("meetId") meetId: Int,
        @Body requestVoteTimeDto: RequestVotePlaceDto
    ): BaseResponse<Unit>

    @GET("/api/v1/meets/{meetId}/places")
    suspend fun getVotePlace(
        @Path("meetId") meetId: Int
    ): BaseResponse<ResponseVotePlaceDto>

    @PATCH("/api/v1/meets/{meetId}/times/confirm")
    suspend fun confirmMeetTime(
        @Path("meetId") meetId: Int,
        @Body requestConfirmTimeDto: RequestConfirmTimeDto
    ): BaseResponse<String>

    @PATCH("/api/v1/meets/{meetId}/places/confirm")
    suspend fun confirmMeetPlace(
        @Path("meetId") meetId: Int,
        @Body requestConfirmPlaceDto: RequestConfirmPlaceDto
    ): BaseResponse<String>
}