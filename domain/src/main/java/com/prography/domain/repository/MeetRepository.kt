package com.prography.domain.repository

import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.request.VotePlaceRequestEntity
import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.model.response.ThemesResponseEntity
import com.prography.domain.model.response.TimePlaceResponseEntity
import kotlinx.coroutines.flow.Flow

interface MeetRepository {
    suspend fun getMeetThemes(): Result<List<ThemesResponseEntity>>
    suspend fun createMeet(
        userId: Int,
        createMeetRequestEntity: CreateMeetRequestEntity
    ): Result<CreateMeetResponseEntity>

    suspend fun getParticipantMeets(userId: Int): Flow<List<MeetsResponseEntity>>
    suspend fun getMeetInformationDetail(userId: Int, meetId: Int): Flow<MeetDetailResponseEntity>
    suspend fun participantMeet(userId: Int, meetId: Int): Result<Unit>
    suspend fun getTimePlaceCandidate(meetId: Int): Result<TimePlaceResponseEntity>
    suspend fun voteTime(
        userId: Int,
        meetId: Int,
        voteTimeRequestEntity: VoteTimeRequestEntity
    ): Result<Unit>

    suspend fun votePlace(
        userId: Int,
        meetId: Int,
        votePlaceRequestEntity: VotePlaceRequestEntity
    ): Result<Unit>
}