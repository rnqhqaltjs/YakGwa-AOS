package com.prography.data.datasourceimpl.remote

import com.prography.data.datasource.remote.MeetRemoteDataSource
import com.prography.data.model.request.RequestCreateMeetDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseCreateMeetDto
import com.prography.data.model.response.ResponseMeetDetailDto
import com.prography.data.model.response.ResponseMeetsDto
import com.prography.data.model.response.ResponseParticipantMeetDto
import com.prography.data.model.response.ResponsePromiseHistoryDto
import com.prography.data.model.response.ResponseThemesDto
import com.prography.data.remote.MeetService
import javax.inject.Inject

class MeetRemoteDataSourceImpl @Inject constructor(
    private val meetService: MeetService
) : MeetRemoteDataSource {
    override suspend fun getMeetThemes(): BaseResponse<List<ResponseThemesDto>> {
        return meetService.getMeetThemes()
    }

    override suspend fun createMeet(
        requestCreateMeetDto: RequestCreateMeetDto
    ): BaseResponse<ResponseCreateMeetDto> {
        return meetService.createMeet(requestCreateMeetDto)
    }

    override suspend fun getParticipantMeets(): BaseResponse<ResponseMeetsDto> {
        return meetService.getParticipantMeets()
    }

    override suspend fun getMeetInformationDetail(
        meetId: Int
    ): BaseResponse<ResponseMeetDetailDto> {
        return meetService.getMeetInformationDetail(meetId)
    }

    override suspend fun participantMeet(meetId: Int): BaseResponse<ResponseParticipantMeetDto> {
        return meetService.participantMeet(meetId)
    }

    override suspend fun getPromiseHistory(): BaseResponse<ResponsePromiseHistoryDto> {
        return meetService.getPromiseHistory()
    }
}