package com.yomo.data.remote

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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface VoteService {
    @GET("/api/v1/meets/{meetId}/placeslots")
    suspend fun getPlaceCandidate(@Path("meetId") meetId: Int): ApiResponse<BaseResponse<ResponsePlaceCandidateDto>>

    @GET("/api/v1/meets/{meetId}/times")
    suspend fun getTimeCandidate(@Path("meetId") meetId: Int): ApiResponse<BaseResponse<ResponseTimeCandidateDto>>

    @POST("/api/v1/meets/{meetId}/times")
    suspend fun voteTime(
        @Path("meetId") meetId: Int,
        @Body requestVoteTimeDto: RequestVoteTimeDto
    ): ApiResponse<Unit>

    @POST("/api/v1/meets/{meetId}/places")
    suspend fun votePlace(
        @Path("meetId") meetId: Int,
        @Body requestVoteTimeDto: RequestVotePlaceDto
    ): ApiResponse<Unit>

    @GET("/api/v1/meets/{meetId}/places")
    suspend fun getVotePlace(
        @Path("meetId") meetId: Int
    ): ApiResponse<BaseResponse<ResponseVotePlaceDto>>

    @PATCH("/api/v1/meets/{meetId}/times/confirm")
    suspend fun confirmMeetTime(
        @Path("meetId") meetId: Int,
        @Body requestConfirmTimeDto: RequestConfirmTimeDto
    ): ApiResponse<BaseResponse<String>>

    @PATCH("/api/v1/meets/{meetId}/places/confirm")
    suspend fun confirmMeetPlace(
        @Path("meetId") meetId: Int,
        @Body requestConfirmPlaceDto: RequestConfirmPlaceDto
    ): ApiResponse<BaseResponse<String>>

    @POST("/api/v1/meets/{meetId}/placeslots")
    suspend fun addPlaceCandidate(
        @Path("meetId") meetId: Int,
        @Body requestPlaceCandidateDto: RequestPlaceCandidateDto
    ): ApiResponse<Unit>
}