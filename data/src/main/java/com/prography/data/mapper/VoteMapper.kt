package com.prography.data.mapper

import com.prography.data.model.request.RequestVotePlaceDto
import com.prography.data.model.request.RequestVoteTimeDto
import com.prography.data.model.response.ResponseTimePlaceDto
import com.prography.data.model.response.ResponseVoteInfoDto
import com.prography.domain.model.request.VotePlaceRequestEntity
import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.model.response.TimePlaceResponseEntity
import com.prography.domain.model.response.VoteInfoResponseEntity

object VoteMapper {
    fun mapperToTimePlaceResponseEntity(responseTimePlaceDto: ResponseTimePlaceDto): TimePlaceResponseEntity {
        return responseTimePlaceDto.run {
            TimePlaceResponseEntity(
                placeItems.map { placeItemDto ->
                    placeItemDto.run {
                        TimePlaceResponseEntity.PlaceItem(
                            this.candidatePlaceId,
                            this.name,
                            this.address,
                            this.description
                        )
                    }
                },
                TimePlaceResponseEntity.TimeItems(
                    TimePlaceResponseEntity.TimeRange(
                        this.timeItems.timeRange.start,
                        this.timeItems.timeRange.end
                    ),
                    TimePlaceResponseEntity.DateRange(
                        this.timeItems.dateRange.start,
                        this.timeItems.dateRange.end
                    )
                )
            )
        }
    }

    fun mapperToRequestVoteTimeDto(voteTimeRequestEntity: VoteTimeRequestEntity): RequestVoteTimeDto {
        return voteTimeRequestEntity.run {
            RequestVoteTimeDto(
                this.possibleSchedules.map { schedule ->
                    schedule.run {
                        RequestVoteTimeDto.PossibleSchedule(
                            this.possibleStartTime,
                            this.possibleEndTime
                        )
                    }
                }
            )
        }
    }

    fun mapperToRequestVotePlaceDto(votePlaceRequestEntity: VotePlaceRequestEntity): RequestVotePlaceDto {
        return votePlaceRequestEntity.run {
            RequestVotePlaceDto(this.candidatePlaceIds)
        }
    }

    fun mapperToVoteInfoResponseEntity(responseVoteInfoDto: ResponseVoteInfoDto): VoteInfoResponseEntity {
        return responseVoteInfoDto.run {
            VoteInfoResponseEntity(
                this.voteStatus,
                this.myPlaceVoteInfo.map { placeInfo ->
                    placeInfo.run {
                        VoteInfoResponseEntity.MyPlaceVoteInfo(
                            this.name,
                            this.address
                        )
                    }
                },
                this.myTimeVoteInfo.map { timeInfo ->
                    timeInfo.run {
                        VoteInfoResponseEntity.MyTimeVoteInfo(
                            this.start,
                            this.end
                        )
                    }
                }
            )
        }
    }
}