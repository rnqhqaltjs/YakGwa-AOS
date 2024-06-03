package com.prography.data.service

import com.prography.data.model.request.RequestCreateMeetDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseMeetsDto
import com.prography.data.model.response.ResponseThemesDto
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
    ): BaseResponse<Unit>

    @GET("/api/v1/users/{userId}/entries")
    suspend fun getParticipantMeets(@Path("userId") userId: Int): BaseResponse<ResponseMeetsDto>
}