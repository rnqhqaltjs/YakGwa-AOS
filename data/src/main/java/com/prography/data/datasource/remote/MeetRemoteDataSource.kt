package com.prography.data.datasource.remote

import com.prography.data.model.request.RequestCreateMeetDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseCreateMeetDto
import com.prography.data.model.response.ResponseMeetDetailDto
import com.prography.data.model.response.ResponseMeetsDto
import com.prography.data.model.response.ResponseParticipantMeetDto
import com.prography.data.model.response.ResponseThemesDto

interface MeetRemoteDataSource {
    suspend fun getMeetThemes(): BaseResponse<List<ResponseThemesDto>>

    suspend fun createMeet(
        requestCreateMeetDto: RequestCreateMeetDto
    ): BaseResponse<ResponseCreateMeetDto>

    suspend fun getParticipantMeets(): BaseResponse<ResponseMeetsDto>

    suspend fun getMeetInformationDetail(
        meetId: Int
    ): BaseResponse<ResponseMeetDetailDto>

    suspend fun participantMeet(
        meetId: Int
    ): BaseResponse<ResponseParticipantMeetDto>
}