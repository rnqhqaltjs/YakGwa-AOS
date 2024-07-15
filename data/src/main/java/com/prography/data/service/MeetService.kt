package com.prography.data.service

import com.prography.data.model.request.RequestCreateMeetDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseCreateMeetDto
import com.prography.data.model.response.ResponseMeetDetailDto
import com.prography.data.model.response.ResponseMeetsDto
import com.prography.data.model.response.ResponseThemesDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MeetService {
    @GET("/api/v1/theme")
    suspend fun getMeetThemes(): BaseResponse<List<ResponseThemesDto>>

    @POST("/api/v1/meets")
    suspend fun createMeet(
        @Body requestCreateMeetDto: RequestCreateMeetDto
    ): BaseResponse<ResponseCreateMeetDto>

    @GET("/api/v1/meets")
    suspend fun getParticipantMeets(): BaseResponse<ResponseMeetsDto>

    @GET("/api/v1/meets/{meetId}")
    suspend fun getMeetInformationDetail(
        @Path("meetId") meetId: Int
    ): BaseResponse<ResponseMeetDetailDto>

    @POST("/api/v1/users/{userId}/meets/{meetId}")
    suspend fun participantMeet(
        @Path("userId") userId: Int,
        @Path("meetId") meetId: Int
    ): BaseResponse<Unit>

}