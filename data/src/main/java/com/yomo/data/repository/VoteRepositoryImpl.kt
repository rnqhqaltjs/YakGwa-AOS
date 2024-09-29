package com.yomo.data.repository

import com.yomo.data.datasource.remote.VoteRemoteDataSource
import com.yomo.data.mapper.VoteMapper
import com.yomo.domain.model.request.ConfirmPlaceRequestEntity
import com.yomo.domain.model.request.ConfirmTimeRequestEntity
import com.yomo.domain.model.request.PlaceCandidateRequestEntity
import com.yomo.domain.model.request.VotePlaceRequestEntity
import com.yomo.domain.model.request.VoteTimeRequestEntity
import com.yomo.domain.model.response.PlaceCandidateResponseEntity
import com.yomo.domain.model.response.TimeCandidateResponseEntity
import com.yomo.domain.model.response.VotePlaceResponseEntity
import com.yomo.domain.repository.VoteRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val voteRemoteDataSource: VoteRemoteDataSource
) : VoteRepository {
    override suspend fun getPlaceCandidate(meetId: Int): ApiResponse<List<PlaceCandidateResponseEntity>> {
        val response = voteRemoteDataSource.getPlaceCandidate(meetId)

        return response.mapSuccess {
            VoteMapper.mapperToPlaceCandidateResponseEntity(this.result)
        }
    }

    override suspend fun getTimeCandidate(meetId: Int): ApiResponse<TimeCandidateResponseEntity> {
        val response = voteRemoteDataSource.getTimeCandidate(meetId)

        return response.mapSuccess {
            VoteMapper.mapperToTimeCandidateResponseEntity(this.result)
        }
    }

    override suspend fun voteTime(
        meetId: Int,
        voteTimeRequestEntity: VoteTimeRequestEntity
    ): ApiResponse<Unit> {
        return voteRemoteDataSource.voteTime(
            meetId,
            VoteMapper.mapperToRequestVoteTimeDto(voteTimeRequestEntity)
        )
    }

    override suspend fun votePlace(
        meetId: Int,
        votePlaceRequestEntity: VotePlaceRequestEntity
    ): ApiResponse<Unit> {
        return voteRemoteDataSource.votePlace(
            meetId,
            VoteMapper.mapperToRequestVotePlaceDto(votePlaceRequestEntity)
        )
    }

    override suspend fun confirmMeetTime(
        meetId: Int,
        confirmTimeRequestEntity: ConfirmTimeRequestEntity
    ): ApiResponse<String> {
        val response = voteRemoteDataSource.confirmMeetTime(
            meetId,
            VoteMapper.mapperToRequestConfirmTimeDto(confirmTimeRequestEntity)
        )
        return response.mapSuccess {
            this.message
        }
    }

    override suspend fun confirmMeetPlace(
        meetId: Int,
        confirmPlaceRequestEntity: ConfirmPlaceRequestEntity
    ): ApiResponse<String> {
        val response = voteRemoteDataSource.confirmMeetPlace(
            meetId,
            VoteMapper.mapperToRequestConfirmPlaceDto(confirmPlaceRequestEntity)
        )
        return response.mapSuccess {
            this.message
        }
    }

    override suspend fun addPlaceCandidate(
        meetId: Int,
        placeCandidateRequestEntity: PlaceCandidateRequestEntity
    ): ApiResponse<Unit> {
        return voteRemoteDataSource.addPlaceCandidate(
            meetId,
            VoteMapper.mapperToRequestPlaceCandidateDto(placeCandidateRequestEntity)
        )
    }

    override suspend fun getVotePlace(meetId: Int): ApiResponse<VotePlaceResponseEntity> {
        val response = voteRemoteDataSource.getVotePlace(meetId)

        return response.mapSuccess {
            VoteMapper.mapperToVotePlaceResponseEntity(this.result)
        }
    }
}