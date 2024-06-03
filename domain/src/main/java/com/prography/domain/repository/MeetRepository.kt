package com.prography.domain.repository

import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.model.response.ThemesResponseEntity
import kotlinx.coroutines.flow.Flow

interface MeetRepository {
    suspend fun getMeetThemes(): Result<List<ThemesResponseEntity>>

    suspend fun createMeet(
        userId: Int,
        createMeetRequestEntity: CreateMeetRequestEntity
    ): Result<Unit>

    suspend fun getParticipantMeets(userId: Int): Flow<List<MeetsResponseEntity>>

}