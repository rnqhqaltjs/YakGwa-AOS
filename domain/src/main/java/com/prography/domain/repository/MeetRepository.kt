package com.prography.domain.repository

import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.model.response.ParticipantMeetResponseEntity
import com.prography.domain.model.response.PromiseHistoryResponseEntity
import com.prography.domain.model.response.ThemesResponseEntity

interface MeetRepository {
    suspend fun getMeetThemes(): Result<List<ThemesResponseEntity>>
    suspend fun createMeet(createMeetRequestEntity: CreateMeetRequestEntity): Result<CreateMeetResponseEntity>
    suspend fun getParticipantMeets(): Result<List<MeetsResponseEntity>>
    suspend fun getMeetInformationDetail(meetId: Int): Result<MeetDetailResponseEntity>
    suspend fun participantMeet(meetId: Int): Result<ParticipantMeetResponseEntity>
    suspend fun getPromiseHistory(): Result<List<PromiseHistoryResponseEntity>>
}