package com.prography.domain.repository

import com.prography.domain.model.request.VotePlaceRequestEntity
import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.model.response.TimePlaceResponseEntity
import com.prography.domain.model.response.VoteInfoResponseEntity

interface VoteRepository {
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

    suspend fun getVoteInfo(
        userId: Int,
        meetId: Int
    ): Result<VoteInfoResponseEntity>
}