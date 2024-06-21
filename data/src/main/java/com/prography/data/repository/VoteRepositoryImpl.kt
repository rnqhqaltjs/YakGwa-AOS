package com.prography.data.repository

import com.prography.data.datasource.remote.VoteRemoteDataSource
import com.prography.data.mapper.VoteMapper
import com.prography.domain.model.request.VotePlaceRequestEntity
import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.model.response.TimePlaceResponseEntity
import com.prography.domain.model.response.VoteInfoResponseEntity
import com.prography.domain.repository.VoteRepository
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val voteRemoteDataSource: VoteRemoteDataSource
) : VoteRepository {
    override suspend fun getTimePlaceCandidate(meetId: Int): Result<TimePlaceResponseEntity> {
        val response = voteRemoteDataSource.getTimePlaceCandidate(meetId)

        return runCatching {
            VoteMapper.mapperToTimePlaceResponseEntity(response.result)
        }
    }

    override suspend fun voteTime(
        userId: Int,
        meetId: Int,
        voteTimeRequestEntity: VoteTimeRequestEntity
    ): Result<Unit> {
        val response = voteRemoteDataSource.voteTime(
            userId,
            meetId,
            VoteMapper.mapperToRequestVoteTimeDto(voteTimeRequestEntity)
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
        val response = voteRemoteDataSource.votePlace(
            userId,
            meetId,
            VoteMapper.mapperToRequestVotePlaceDto(votePlaceRequestEntity)
        )

        return runCatching {
            response.result
        }
    }

    override suspend fun getVoteInfo(userId: Int, meetId: Int): Result<VoteInfoResponseEntity> {
        val response = voteRemoteDataSource.getVoteInfo(userId, meetId)

        return runCatching {
            VoteMapper.mapperToVoteInfoResponseEntity(response.result)
        }
    }
}
