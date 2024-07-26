package com.prography.data.mapper

import com.prography.data.model.request.RequestCreateMeetDto
import com.prography.data.model.response.ResponseCreateMeetDto
import com.prography.data.model.response.ResponseMeetDetailDto
import com.prography.data.model.response.ResponseMeetsDto
import com.prography.data.model.response.ResponseParticipantMeetDto
import com.prography.data.model.response.ResponsePromiseHistoryDto
import com.prography.data.model.response.ResponseThemesDto
import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.model.response.ParticipantMeetResponseEntity
import com.prography.domain.model.response.PromiseHistoryResponseEntity
import com.prography.domain.model.response.ThemesResponseEntity

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
                            this.meetId
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
                        this.themeName,
                    )
                },
                this.participantInfo.map { participant ->
                    participant.run {
                        MeetDetailResponseEntity.ParticipantInfo(
                            this.meetRole,
                            this.imageUrl,
                            this.participantId
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
                    })
            }
        }
    }
}