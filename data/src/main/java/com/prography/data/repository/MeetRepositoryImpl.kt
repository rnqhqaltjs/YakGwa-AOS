package com.prography.data.repository

import com.prography.data.datasource.remote.MeetRemoteDataSource
import com.prography.data.mapper.MeetMapper
import com.prography.domain.model.request.CreateMeetRequestEntity
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
    ): Result<Unit> {
        val response = meetRemoteDataSource.createMeet(
            userId,
            MeetMapper.mapperToRequestCreateMeetDto(createMeetRequestEntity)
        )

        return runCatching {
            response.result
        }
    }
}
