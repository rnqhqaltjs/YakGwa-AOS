package com.prography.data.repository

import com.prography.data.datasource.remote.VoteRemoteDataSource
import com.prography.data.mapper.VoteMapper
import com.prography.domain.model.request.ConfirmPlaceRequestEntity
import com.prography.domain.model.request.ConfirmTimeRequestEntity
import com.prography.domain.model.request.PlaceCandidateRequestEntity
import com.prography.domain.model.request.VotePlaceRequestEntity
import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.model.response.PlaceCandidateResponseEntity
import com.prography.domain.model.response.TimeCandidateResponseEntity
import com.prography.domain.model.response.VotePlaceResponseEntity
import com.prography.domain.repository.VoteRepository
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val voteRemoteDataSource: VoteRemoteDataSource
) : VoteRepository {
    override suspend fun getPlaceCandidate(meetId: Int): Result<List<PlaceCandidateResponseEntity>> {
        val response = voteRemoteDataSource.getPlaceCandidate(meetId)

        return runCatching {
            VoteMapper.mapperToPlaceCandidateResponseEntity(response.result)
        }
    }

    override suspend fun getTimeCandidate(meetId: Int): Result<TimeCandidateResponseEntity> {
        val response = voteRemoteDataSource.getTimeCandidate(meetId)

        return runCatching {
            VoteMapper.mapperToTimeCandidateResponseEntity(response.result)
        }
    }

    override suspend fun voteTime(
        meetId: Int,
        voteTimeRequestEntity: VoteTimeRequestEntity
    ): Result<Unit> {
        val response = voteRemoteDataSource.voteTime(
            meetId,
            VoteMapper.mapperToRequestVoteTimeDto(voteTimeRequestEntity)
        )

        return runCatching {
            response.result
        }
    }

    override suspend fun votePlace(
        meetId: Int,
        votePlaceRequestEntity: VotePlaceRequestEntity
    ): Result<Unit> {
        val response = voteRemoteDataSource.votePlace(
            meetId,
            VoteMapper.mapperToRequestVotePlaceDto(votePlaceRequestEntity)
        )

        return runCatching {
            response.result
        }
    }

    override suspend fun confirmMeetTime(
        meetId: Int,
        confirmTimeRequestEntity: ConfirmTimeRequestEntity
    ): Result<String> {
        val response = voteRemoteDataSource.confirmMeetTime(
            meetId,
            VoteMapper.mapperToRequestConfirmTimeDto(confirmTimeRequestEntity)
        )

        return runCatching {
            response.result
        }
    }

    override suspend fun confirmMeetPlace(
        meetId: Int,
        confirmPlaceRequestEntity: ConfirmPlaceRequestEntity
    ): Result<String> {
        val response = voteRemoteDataSource.confirmMeetPlace(
            meetId,
            VoteMapper.mapperToRequestConfirmPlaceDto(confirmPlaceRequestEntity)
        )

        return runCatching {
            response.result
        }
    }

    override suspend fun addPlaceCandidate(
        meetId: Int,
        placeCandidateRequestEntity: PlaceCandidateRequestEntity
    ): Result<Unit> {
        val response = voteRemoteDataSource.addPlaceCandidate(
            meetId,
            VoteMapper.mapperToRequestPlaceCandidateDto(placeCandidateRequestEntity)
        )

        return runCatching {
            response
        }
    }

    override suspend fun getVotePlace(meetId: Int): Result<VotePlaceResponseEntity> {
        val response = voteRemoteDataSource.getVotePlace(meetId)

        return runCatching {
            VoteMapper.mapperToVotePlaceResponseEntity(response.result)
        }
    }
}
