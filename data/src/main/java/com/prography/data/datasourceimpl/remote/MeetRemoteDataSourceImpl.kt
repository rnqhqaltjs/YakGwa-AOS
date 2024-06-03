package com.prography.data.datasourceimpl.remote

import com.prography.data.datasource.remote.MeetRemoteDataSource
import com.prography.data.model.request.RequestCreateMeetDto
import com.prography.data.model.response.BaseResponse
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
    ): BaseResponse<Unit> {
        return meetService.createMeet(userId, requestCreateMeetDto)
    }

    override suspend fun getParticipantMeets(userId: Int): BaseResponse<ResponseMeetsDto> {
        return meetService.getParticipantMeets(userId)
    }
}