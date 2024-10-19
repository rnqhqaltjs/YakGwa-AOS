package com.yomo.data.mapper

import com.yomo.data.model.request.RequestCreateMeetDto
import com.yomo.data.model.response.ResponseCreateMeetDto
import com.yomo.data.model.response.ResponseMeetDetailDto
import com.yomo.data.model.response.ResponseMeetsDto
import com.yomo.data.model.response.ResponseParticipantMeetDto
import com.yomo.data.model.response.ResponsePromiseHistoryDto
import com.yomo.data.model.response.ResponseThemesDto
import com.yomo.domain.model.request.CreateMeetRequestEntity
import com.yomo.domain.model.response.CreateMeetResponseEntity
import com.yomo.domain.model.response.MeetDetailResponseEntity
import com.yomo.domain.model.response.MeetsResponseEntity
import com.yomo.domain.model.response.ParticipantMeetResponseEntity
import com.yomo.domain.model.response.PromiseHistoryResponseEntity
import com.yomo.domain.model.response.ThemesResponseEntity

object MeetMapper {
    fun mapperToThemesResponseEntity(responseThemesDto: List<ResponseThemesDto>): List<ThemesResponseEntity> {
        return responseThemesDto.map { meetThemeInfo ->
            meetThemeInfo.run {
                ThemesResponseEntity(this.id, this.name)
            }
        }
    }

    fun mapperToRequestCreateMeetDto(createMeetRequestEntity: CreateMeetRequestEntity): RequestCreateMeetDto {
        return createMeetRequestEntity.run {
            RequestCreateMeetDto(
                RequestCreateMeetDto.MeetInfo(
                    this.meetTitle,
                    this.description,
                    this.meetThemeId,
                    this.confirmPlace,
                    this.placeInfo.map { place ->
                        place.run {
                            RequestCreateMeetDto.PlaceInfo(
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
                        }
                    },
                    this.voteDate?.run {
                        RequestCreateMeetDto.VoteDate(
                            this.startVoteDate,
                            this.endVoteDate
                        )
                    },
                    this.meetTime
                )
            )
        }
    }

    fun mapperToCreateMeetResponseEntity(responseCreateMeetDto: ResponseCreateMeetDto): CreateMeetResponseEntity {
        return responseCreateMeetDto.run {
            CreateMeetResponseEntity(this.meetId)
        }
    }

    fun mapperToMeetsResponseEntity(responseMeetsDto: ResponseMeetsDto): List<MeetsResponseEntity> {
        return responseMeetsDto.meetInfosWithStatus.map { entryInfo ->
            entryInfo.run {
                MeetsResponseEntity(
                    this.meetStatus,
                    this.meetInfo.run {
                        MeetsResponseEntity.MeetInfo(
                            this.meetThemeName,
                            this.meetDateTime,
                            this.placeName,
                            this.meetTitle,
                            this.meetId,
                            this.description
                        )
                    }
                )
            }
        }
    }

    fun mapperToMeetDetailResponseEntity(responseMeetDetailDto: ResponseMeetDetailDto): MeetDetailResponseEntity {
        return responseMeetDetailDto.run {
            MeetDetailResponseEntity(
                this.meetInfo.run {
                    MeetDetailResponseEntity.MeetInfo(
                        this.meetTitle,
                        this.description,
                        this.themeName,
                    )
                },
                this.participantInfo.map { participant ->
                    participant.run {
                        MeetDetailResponseEntity.ParticipantInfo(
                            this.meetRole,
                            this.imageUrl,
                            this.name
                        )
                    }
                }
            )
        }
    }

    fun mapperToParticipantMeetResponseEntity(responseParticipantMeetDto: ResponseParticipantMeetDto): ParticipantMeetResponseEntity {
        return responseParticipantMeetDto.run {
            ParticipantMeetResponseEntity(this.participantId)
        }
    }

    fun mapperToPromiseHistoryResponseEntity(responsePromiseHistoryDto: ResponsePromiseHistoryDto): List<PromiseHistoryResponseEntity> {
        return responsePromiseHistoryDto.meetInfosWithStatus.map { meetInfo ->
            meetInfo.run {
                PromiseHistoryResponseEntity(
                    this.meetStatus,
                    this.meetInfo.run {
                        PromiseHistoryResponseEntity.MeetInfo(
                            this.meetThemeName,
                            this.meetDateTime,
                            this.placeName,
                            this.meetTitle,
                            this.meetId
                        )
                    }, this.description
                )
            }
        }
    }
}