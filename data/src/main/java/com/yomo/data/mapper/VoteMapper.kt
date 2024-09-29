package com.yomo.data.mapper

import com.yomo.data.model.request.RequestConfirmPlaceDto
import com.yomo.data.model.request.RequestConfirmTimeDto
import com.yomo.data.model.request.RequestPlaceCandidateDto
import com.yomo.data.model.request.RequestVotePlaceDto
import com.yomo.data.model.request.RequestVoteTimeDto
import com.yomo.data.model.response.ResponsePlaceCandidateDto
import com.yomo.data.model.response.ResponseTimeCandidateDto
import com.yomo.data.model.response.ResponseVotePlaceDto
import com.yomo.domain.model.request.ConfirmPlaceRequestEntity
import com.yomo.domain.model.request.ConfirmTimeRequestEntity
import com.yomo.domain.model.request.PlaceCandidateRequestEntity
import com.yomo.domain.model.request.VotePlaceRequestEntity
import com.yomo.domain.model.request.VoteTimeRequestEntity
import com.yomo.domain.model.response.PlaceCandidateResponseEntity
import com.yomo.domain.model.response.TimeCandidateResponseEntity
import com.yomo.domain.model.response.VotePlaceResponseEntity

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
                            this.placeSlotId,
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

    fun mapperToRequestPlaceCandidateDto(placeCandidateRequestEntity: PlaceCandidateRequestEntity): RequestPlaceCandidateDto {
        return placeCandidateRequestEntity.run {
            RequestPlaceCandidateDto(
                RequestPlaceCandidateDto.PlaceInfo(
                    this.title,
                    this.link,
                    this.category,
                    this.description,
                    this.telephone,
                    this.address,
                    this.roadAddress,
                    this.mapx,
                    this.mapy
                )
            )
        }
    }
}