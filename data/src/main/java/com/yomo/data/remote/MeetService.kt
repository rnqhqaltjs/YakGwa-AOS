package com.yomo.data.remote

import com.yomo.data.model.request.RequestCreateMeetDto
import com.yomo.data.model.response.BaseResponse
import com.yomo.data.model.response.ResponseCreateMeetDto
import com.yomo.data.model.response.ResponseMeetDetailDto
import com.yomo.data.model.response.ResponseMeetsDto
import com.yomo.data.model.response.ResponseParticipantMeetDto
import com.yomo.data.model.response.ResponsePromiseHistoryDto
import com.yomo.data.model.response.ResponseThemesDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MeetService {
    @GET("/api/v1/theme")
    suspend fun getMeetThemes(): ApiResponse<BaseResponse<List<ResponseThemesDto>>>

    @POST("/api/v1/meets")
    suspend fun createMeet(
        @Body requestCreateMeetDto: RequestCreateMeetDto
    ): ApiResponse<BaseResponse<ResponseCreateMeetDto>>

    @GET("/api/v1/meets")
    suspend fun getParticipantMeets(): ApiResponse<BaseResponse<ResponseMeetsDto>>

    @GET("/api/v1/meets/{meetId}")
    suspend fun getMeetInformationDetail(
        @Path("meetId") meetId: Int
    ): ApiResponse<BaseResponse<ResponseMeetDetailDto>>

    @POST("/api/v1/meets/{meetId}")
    suspend fun participantMeet(
        @Path("meetId") meetId: Int
    ): ApiResponse<BaseResponse<ResponseParticipantMeetDto>>

    @GET("/api/v1/meets/record")
    suspend fun getPromiseHistory(): ApiResponse<BaseResponse<ResponsePromiseHistoryDto>>
}