package com.prography.data.service

import com.prography.data.model.request.RequestCreateMeetDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseCreateMeetDto
import com.prography.data.model.response.ResponseMeetDetailDto
import com.prography.data.model.response.ResponseMeetsDto
import com.prography.data.model.response.ResponseThemesDto
import com.prography.data.model.response.ResponseTimePlaceDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MeetService {
    @GET("/api/v1/meetThemes")
    suspend fun getMeetThemes(): BaseResponse<ResponseThemesDto>

    @POST("/api/v1/users/{userId}/meets")
    suspend fun createMeet(
        @Path("userId") userId: Int,
        @Body requestCreateMeetDto: RequestCreateMeetDto
    ): BaseResponse<ResponseCreateMeetDto>

    @GET("/api/v1/users/{userId}/entries")
    suspend fun getParticipantMeets(@Path("userId") userId: Int): BaseResponse<ResponseMeetsDto>

    @GET("/api/v1/users/{userId}/meets/{meetId}")
    suspend fun getMeetInformationDetail(
        @Path("userId") userId: Int,
        @Path("meetId") meetId: Int
    ): BaseResponse<ResponseMeetDetailDto>

    @POST("/api/v1/users/{userId}/meets/{meetId}")
    suspend fun participantMeet(
        @Path("userId") userId: Int,
        @Path("meetId") meetId: Int
    ): BaseResponse<Unit>

    @GET("/api/v1/meets/{meetId}/vote/items")
    suspend fun getTimePlaceCandidate(@Path("meetId") meetId: Int): BaseResponse<ResponseTimePlaceDto>
}