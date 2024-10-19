package com.yomo.data.datasource.remote

import com.yomo.data.model.request.RequestCreateMeetDto
import com.yomo.data.model.response.BaseResponse
import com.yomo.data.model.response.ResponseCreateMeetDto
import com.yomo.data.model.response.ResponseMeetDetailDto
import com.yomo.data.model.response.ResponseMeetsDto
import com.yomo.data.model.response.ResponseParticipantMeetDto
import com.yomo.data.model.response.ResponsePromiseHistoryDto
import com.yomo.data.model.response.ResponseThemesDto
import com.skydoves.sandwich.ApiResponse

interface MeetRemoteDataSource {
    suspend fun getMeetThemes(): ApiResponse<BaseResponse<List<ResponseThemesDto>>>

    suspend fun createMeet(
        requestCreateMeetDto: RequestCreateMeetDto
    ): ApiResponse<BaseResponse<ResponseCreateMeetDto>>

    suspend fun getParticipantMeets(): ApiResponse<BaseResponse<ResponseMeetsDto>>

    suspend fun getMeetInformationDetail(
        meetId: Int
    ): ApiResponse<BaseResponse<ResponseMeetDetailDto>>

    suspend fun participantMeet(
        meetId: Int
    ): ApiResponse<BaseResponse<ResponseParticipantMeetDto>>

    suspend fun getPromiseHistory(): ApiResponse<BaseResponse<ResponsePromiseHistoryDto>>
}