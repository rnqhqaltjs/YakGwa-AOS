package com.prography.data.repository

import com.prography.data.datasource.remote.MeetRemoteDataSource
import com.prography.data.mapper.MeetMapper
import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.model.response.ParticipantMeetResponseEntity
import com.prography.domain.model.response.PromiseHistoryResponseEntity
import com.prography.domain.model.response.ThemesResponseEntity
import com.prography.domain.repository.MeetRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import javax.inject.Inject

class MeetRepositoryImpl @Inject constructor(
    private val meetRemoteDataSource: MeetRemoteDataSource
) : MeetRepository {
    override suspend fun getMeetThemes(): ApiResponse<List<ThemesResponseEntity>> {
        val response = meetRemoteDataSource.getMeetThemes()

        return response.mapSuccess {
            MeetMapper.mapperToThemesResponseEntity(this.result)
        }
    }

    override suspend fun createMeet(
        createMeetRequestEntity: CreateMeetRequestEntity
    ): ApiResponse<CreateMeetResponseEntity> {
        val response = meetRemoteDataSource.createMeet(
            MeetMapper.mapperToRequestCreateMeetDto(createMeetRequestEntity)
        )

        return response.mapSuccess {
            MeetMapper.mapperToCreateMeetResponseEntity(this.result)
        }
    }

    override suspend fun getParticipantMeets(): ApiResponse<List<MeetsResponseEntity>> {
        val response = meetRemoteDataSource.getParticipantMeets()

        return response.mapSuccess {
            MeetMapper.mapperToMeetsResponseEntity(this.result)
        }
    }

    override suspend fun getMeetInformationDetail(
        meetId: Int
    ): ApiResponse<MeetDetailResponseEntity> {
        val response = meetRemoteDataSource.getMeetInformationDetail(meetId)

        return response.mapSuccess {
            MeetMapper.mapperToMeetDetailResponseEntity(this.result)
        }
    }

    override suspend fun participantMeet(meetId: Int): ApiResponse<ParticipantMeetResponseEntity> {
        val response = meetRemoteDataSource.participantMeet(meetId)

        return response.mapSuccess {
            MeetMapper.mapperToParticipantMeetResponseEntity(this.result)
        }
    }

    override suspend fun getPromiseHistory(): ApiResponse<List<PromiseHistoryResponseEntity>> {
        val response = meetRemoteDataSource.getPromiseHistory()

        return response.mapSuccess {
            MeetMapper.mapperToPromiseHistoryResponseEntity(this.result)
        }
    }
}
