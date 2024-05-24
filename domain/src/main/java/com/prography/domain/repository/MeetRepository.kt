package com.prography.domain.repository

import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.ThemesResponseEntity

interface MeetRepository {
    suspend fun getMeetThemes(): Result<List<ThemesResponseEntity>>

    suspend fun createMeet(
        userId: Int,
        createMeetRequestEntity: CreateMeetRequestEntity
    ): Result<Unit>

}