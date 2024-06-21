package com.prography.data.datasourceimpl.remote

import com.prography.data.datasource.remote.MeetRemoteDataSource
import com.prography.data.model.request.RequestCreateMeetDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseCreateMeetDto
import com.prography.data.model.response.ResponseMeetDetailDto
import com.prography.data.model.response.ResponseMeetsDto
import com.prography.data.model.response.ResponseThemesDto
import com.prography.data.service.MeetService
import javax.inject.Inject

class MeetRemoteDataSourceImpl @Inject constructor(
    private val meetService: MeetService
) : MeetRemoteDataSource {
    override suspend fun getMeetThemes(): BaseResponse<ResponseThemesDto> {
        return meetService.getMeetThemes()
    }

    override suspend fun createMeet(
        userId: Int,
        requestCreateMeetDto: RequestCreateMeetDto
    ): BaseResponse<ResponseCreateMeetDto> {
        return meetService.createMeet(userId, requestCreateMeetDto)
    }

    override suspend fun getParticipantMeets(userId: Int): BaseResponse<ResponseMeetsDto> {
        return meetService.getParticipantMeets(userId)
    }

    override suspend fun getMeetInformationDetail(
        userId: Int,
        meetId: Int
    ): BaseResponse<ResponseMeetDetailDto> {
        return meetService.getMeetInformationDetail(userId, meetId)
    }

    override suspend fun participantMeet(userId: Int, meetId: Int): BaseResponse<Unit> {
        return meetService.participantMeet(userId, meetId)
    }
}