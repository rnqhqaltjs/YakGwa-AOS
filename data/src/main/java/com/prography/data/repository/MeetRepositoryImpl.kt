package com.prography.data.repository

import com.prography.data.datasource.remote.MeetRemoteDataSource
import com.prography.data.mapper.MeetMapper
import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.model.response.ThemesResponseEntity
import com.prography.domain.repository.MeetRepository
import javax.inject.Inject

class MeetRepositoryImpl @Inject constructor(
    private val meetRemoteDataSource: MeetRemoteDataSource
) : MeetRepository {
    override suspend fun getMeetThemes(): Result<List<ThemesResponseEntity>> {
        val response = meetRemoteDataSource.getMeetThemes()

        return runCatching {
            MeetMapper.mapperToThemesResponseEntity(response.result)
        }
    }

    override suspend fun createMeet(
        userId: Int,
        createMeetRequestEntity: CreateMeetRequestEntity
    ): Result<CreateMeetResponseEntity> {
        val response = meetRemoteDataSource.createMeet(
            userId,
            MeetMapper.mapperToRequestCreateMeetDto(createMeetRequestEntity)
        )

        return runCatching {
            MeetMapper.mapperToCreateMeetResponseEntity(response.result)
        }
    }

    override suspend fun getParticipantMeets(userId: Int): Result<List<MeetsResponseEntity>> {
        val response = meetRemoteDataSource.getParticipantMeets(userId)

        return runCatching {
            MeetMapper.mapperToMeetsResponseEntity(response.result)
        }
    }

    override suspend fun getMeetInformationDetail(
        userId: Int,
        meetId: Int
    ): Result<MeetDetailResponseEntity> {
        val response = meetRemoteDataSource.getMeetInformationDetail(userId, meetId)

        return runCatching {
            MeetMapper.mapperToMeetDetailResponseEntity(
                response.result
            )
        }
    }

    override suspend fun participantMeet(userId: Int, meetId: Int): Result<Unit> {
        val response = meetRemoteDataSource.participantMeet(userId, meetId)

        return runCatching {
            response.result
        }
    }
}
