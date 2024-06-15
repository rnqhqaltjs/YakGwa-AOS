package com.prography.data.repository

import com.prography.data.datasource.remote.MeetRemoteDataSource
import com.prography.data.mapper.MeetMapper
import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.request.VotePlaceRequestEntity
import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.model.response.ThemesResponseEntity
import com.prography.domain.model.response.TimePlaceResponseEntity
import com.prography.domain.repository.MeetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    override suspend fun getParticipantMeets(userId: Int): Flow<List<MeetsResponseEntity>> = flow {
        val result = runCatching {
            MeetMapper.mapperToMeetsResponseEntity(meetRemoteDataSource.getParticipantMeets(userId).result)
        }
        emit(result.getOrThrow())
    }

    override suspend fun getMeetInformationDetail(
        userId: Int,
        meetId: Int
    ): Flow<MeetDetailResponseEntity> = flow {
        val result = runCatching {
            MeetMapper.mapperToMeetDetailResponseEntity(
                meetRemoteDataSource.getMeetInformationDetail(
                    userId,
                    meetId
                ).result
            )
        }
        emit(result.getOrThrow())
    }

    override suspend fun participantMeet(userId: Int, meetId: Int): Result<Unit> {
        val response = meetRemoteDataSource.participantMeet(userId, meetId)

        return runCatching {
            response.result
        }
    }

    override suspend fun getTimePlaceCandidate(meetId: Int): Result<TimePlaceResponseEntity> {
        val response = meetRemoteDataSource.getTimePlaceCandidate(meetId)

        return runCatching {
            MeetMapper.mapperToTimePlaceResponseEntity(response.result)
        }
    }

    override suspend fun voteTime(
        userId: Int,
        meetId: Int,
        voteTimeRequestEntity: VoteTimeRequestEntity
    ): Result<Unit> {
        val response = meetRemoteDataSource.voteTime(
            userId,
            meetId,
            MeetMapper.mapperToRequestVoteTimeDto(voteTimeRequestEntity)
        )

        return runCatching {
            response.result
        }
    }

    override suspend fun votePlace(
        userId: Int,
        meetId: Int,
        votePlaceRequestEntity: VotePlaceRequestEntity
    ): Result<Unit> {
        val response = meetRemoteDataSource.votePlace(
            userId,
            meetId,
            MeetMapper.mapperToRequestVotePlaceDto(votePlaceRequestEntity)
        )

        return runCatching {
            response.result
        }
    }
}
