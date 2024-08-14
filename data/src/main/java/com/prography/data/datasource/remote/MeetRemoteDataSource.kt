package com.prography.data.datasource.remote

import com.prography.data.model.request.RequestCreateMeetDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseCreateMeetDto
import com.prography.data.model.response.ResponseMeetDetailDto
import com.prography.data.model.response.ResponseMeetsDto
import com.prography.data.model.response.ResponseParticipantMeetDto
import com.prography.data.model.response.ResponsePromiseHistoryDto
import com.prography.data.model.response.ResponseThemesDto
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