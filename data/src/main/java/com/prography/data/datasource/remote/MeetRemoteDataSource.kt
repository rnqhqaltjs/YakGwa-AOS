package com.prography.data.datasource.remote

import com.prography.data.model.request.RequestCreateMeetDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseCreateMeetDto
import com.prography.data.model.response.ResponseMeetDetailDto
import com.prography.data.model.response.ResponseMeetsDto
import com.prography.data.model.response.ResponseThemesDto

interface MeetRemoteDataSource {
    suspend fun getMeetThemes(): BaseResponse<ResponseThemesDto>

    suspend fun createMeet(
        userId: Int,
        requestCreateMeetDto: RequestCreateMeetDto
    ): BaseResponse<ResponseCreateMeetDto>

    suspend fun getParticipantMeets(userId: Int): BaseResponse<ResponseMeetsDto>

    suspend fun getMeetInformationDetail(
        userId: Int,
        meetId: Int
    ): BaseResponse<ResponseMeetDetailDto>

    suspend fun participantMeet(
        userId: Int,
        meetId: Int
    ): BaseResponse<Unit>
}