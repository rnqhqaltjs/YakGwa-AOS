package com.prography.domain.repository

import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.model.response.ParticipantMeetResponseEntity
import com.prography.domain.model.response.PromiseHistoryResponseEntity
import com.prography.domain.model.response.ThemesResponseEntity
import com.skydoves.sandwich.ApiResponse

interface MeetRepository {
    suspend fun getMeetThemes(): ApiResponse<List<ThemesResponseEntity>>
    suspend fun createMeet(createMeetRequestEntity: CreateMeetRequestEntity): ApiResponse<CreateMeetResponseEntity>
    suspend fun getParticipantMeets(): ApiResponse<List<MeetsResponseEntity>>
    suspend fun getMeetInformationDetail(meetId: Int): ApiResponse<MeetDetailResponseEntity>
    suspend fun participantMeet(meetId: Int): ApiResponse<ParticipantMeetResponseEntity>
    suspend fun getPromiseHistory(): ApiResponse<List<PromiseHistoryResponseEntity>>
}