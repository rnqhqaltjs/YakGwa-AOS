package com.prography.data.repository

import com.prography.data.datasource.remote.MeetRemoteDataSource
import com.prography.data.mapper.MeetMapper
import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.model.response.ParticipantMeetResponseEntity
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
        createMeetRequestEntity: CreateMeetRequestEntity
    ): Result<CreateMeetResponseEntity> {
        val response = meetRemoteDataSource.createMeet(
            MeetMapper.mapperToRequestCreateMeetDto(createMeetRequestEntity)
        )

        return runCatching {
            MeetMapper.mapperToCreateMeetResponseEntity(response.result)
        }
    }

    override suspend fun getParticipantMeets(): Result<List<MeetsResponseEntity>> {
        val response = meetRemoteDataSource.getParticipantMeets()

        return runCatching {
            MeetMapper.mapperToMeetsResponseEntity(response.result)
        }
    }

    override suspend fun getMeetInformationDetail(
        meetId: Int
    ): Result<MeetDetailResponseEntity> {
        val response = meetRemoteDataSource.getMeetInformationDetail(meetId)

        return runCatching {
            MeetMapper.mapperToMeetDetailResponseEntity(
                response.result
            )
        }
    }

    override suspend fun participantMeet(meetId: Int): Result<ParticipantMeetResponseEntity> {
        val response = meetRemoteDataSource.participantMeet(meetId)

        return runCatching {
            MeetMapper.mapperToParticipantMeetResponseEntity(
                response.result
            )
        }
    }
}
