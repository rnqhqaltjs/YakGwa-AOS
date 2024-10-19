package com.yomo.data.datasourceimpl.remote

import com.yomo.data.datasource.remote.MeetRemoteDataSource
import com.yomo.data.model.request.RequestCreateMeetDto
import com.yomo.data.model.response.BaseResponse
import com.yomo.data.model.response.ResponseCreateMeetDto
import com.yomo.data.model.response.ResponseMeetDetailDto
import com.yomo.data.model.response.ResponseMeetsDto
import com.yomo.data.model.response.ResponseParticipantMeetDto
import com.yomo.data.model.response.ResponsePromiseHistoryDto
import com.yomo.data.model.response.ResponseThemesDto
import com.yomo.data.remote.MeetService
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class MeetRemoteDataSourceImpl @Inject constructor(
    private val meetService: MeetService
) : MeetRemoteDataSource {
    override suspend fun getMeetThemes(): ApiResponse<BaseResponse<List<ResponseThemesDto>>> {
        return meetService.getMeetThemes()
    }

    override suspend fun createMeet(
        requestCreateMeetDto: RequestCreateMeetDto
    ): ApiResponse<BaseResponse<ResponseCreateMeetDto>> {
        return meetService.createMeet(requestCreateMeetDto)
    }

    override suspend fun getParticipantMeets(): ApiResponse<BaseResponse<ResponseMeetsDto>> {
        return meetService.getParticipantMeets()
    }

    override suspend fun getMeetInformationDetail(
        meetId: Int
    ): ApiResponse<BaseResponse<ResponseMeetDetailDto>> {
        return meetService.getMeetInformationDetail(meetId)
    }

    override suspend fun participantMeet(meetId: Int): ApiResponse<BaseResponse<ResponseParticipantMeetDto>> {
        return meetService.participantMeet(meetId)
    }

    override suspend fun getPromiseHistory(): ApiResponse<BaseResponse<ResponsePromiseHistoryDto>> {
        return meetService.getPromiseHistory()
    }
}