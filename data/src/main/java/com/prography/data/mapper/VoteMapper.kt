package com.prography.data.mapper

import com.prography.data.model.request.RequestConfirmPlaceDto
import com.prography.data.model.request.RequestConfirmTimeDto
import com.prography.data.model.request.RequestVotePlaceDto
import com.prography.data.model.request.RequestVoteTimeDto
import com.prography.data.model.response.ResponsePlaceCandidateDto
import com.prography.data.model.response.ResponseTimeCandidateDto
import com.prography.data.model.response.ResponseVotePlaceDto
import com.prography.domain.model.request.ConfirmPlaceRequestEntity
import com.prography.domain.model.request.ConfirmTimeRequestEntity
import com.prography.domain.model.request.VotePlaceRequestEntity
import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.model.response.PlaceCandidateResponseEntity
import com.prography.domain.model.response.TimeCandidateResponseEntity
import com.prography.domain.model.response.VotePlaceResponseEntity

object VoteMapper {
    fun mapperToPlaceCandidateResponseEntity(responsePlaceCandidateDto: ResponsePlaceCandidateDto): List<PlaceCandidateResponseEntity> {
        return responsePlaceCandidateDto.run {
            this.placeSlotOfMeet.map { placeInfo ->
                placeInfo.run {
                    PlaceCandidateResponseEntity(
                        this.placeSlotId,
                        this.placeName,
                        this.placeAddress,
                        this.userInfos?.map { userInfo ->
                            userInfo.run {
                                PlaceCandidateResponseEntity.UserInfos(
                                    this.username,
                                    this.imageUrl
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    fun mapperToTimeCandidateResponseEntity(responseTimeCandidateDto: ResponseTimeCandidateDto): TimeCandidateResponseEntity {
        return responseTimeCandidateDto.run {
            TimeCandidateResponseEntity(
                this.meetStatus,
                this.timeInfos?.map { timeInfo ->
                    timeInfo.run {
                        TimeCandidateResponseEntity.TimeInfo(
                            this.timeId,
                            this.voteTime
                        )
                    }
                },
                this.voteDate?.run {
                    TimeCandidateResponseEntity.VoteDate(
                        this.startVoteDate,
                        this.endVoteDate
                    )
                }
            )

        }
    }

    fun mapperToRequestVoteTimeDto(voteTimeRequestEntity: VoteTimeRequestEntity): RequestVoteTimeDto {
        return voteTimeRequestEntity.run {
            RequestVoteTimeDto(
                this.enableTimes.map { schedule ->
                    schedule.run {
                        RequestVoteTimeDto.EnableTimes(
                            this.enableTime
                        )
                    }
                }
            )
        }
    }

    fun mapperToRequestVotePlaceDto(votePlaceRequestEntity: VotePlaceRequestEntity): RequestVotePlaceDto {
        return votePlaceRequestEntity.run {
            RequestVotePlaceDto(this.currentVotePlaceSlotIds)
        }
    }

    fun mapperToVotePlaceResponseEntity(responseVotePlaceDto: ResponseVotePlaceDto): VotePlaceResponseEntity {
        return responseVotePlaceDto.run {
            VotePlaceResponseEntity(
                this.meetStatus,
                this.placeInfos?.map { placeInfo ->
                    placeInfo.run {
                        VotePlaceResponseEntity.PlaceInfos(
                            this.placeId,
                            this.title,
                            this.roadAddress,
                            this.mapx,
                            this.mapy
                        )
                    }
                }
            )
        }
    }

    fun mapperToRequestConfirmTimeDto(confirmTimeRequestEntity: ConfirmTimeRequestEntity): RequestConfirmTimeDto {
        return confirmTimeRequestEntity.run {
            RequestConfirmTimeDto(this.confirmTimeSlotId)
        }
    }

    fun mapperToRequestConfirmPlaceDto(confirmPlaceRequestEntity: ConfirmPlaceRequestEntity): RequestConfirmPlaceDto {
        return confirmPlaceRequestEntity.run {
            RequestConfirmPlaceDto(this.confirmPlaceSlotId)
        }
    }
}