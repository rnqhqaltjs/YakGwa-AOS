package com.prography.domain.repository

import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.model.response.ThemesResponseEntity

interface MeetRepository {
    suspend fun getMeetThemes(): Result<List<ThemesResponseEntity>>
    suspend fun createMeet(
        userId: Int,
        createMeetRequestEntity: CreateMeetRequestEntity
    ): Result<CreateMeetResponseEntity>

    suspend fun getParticipantMeets(userId: Int): Result<List<MeetsResponseEntity>>
    suspend fun getMeetInformationDetail(userId: Int, meetId: Int): Result<MeetDetailResponseEntity>
    suspend fun participantMeet(userId: Int, meetId: Int): Result<Unit>
}