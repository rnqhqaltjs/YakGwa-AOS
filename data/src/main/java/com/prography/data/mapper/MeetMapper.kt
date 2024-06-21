package com.prography.data.mapper

import com.prography.data.model.request.RequestCreateMeetDto
import com.prography.data.model.response.ResponseCreateMeetDto
import com.prography.data.model.response.ResponseMeetDetailDto
import com.prography.data.model.response.ResponseMeetsDto
import com.prography.data.model.response.ResponseThemesDto
import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.model.response.ThemesResponseEntity

object MeetMapper {
    fun mapperToThemesResponseEntity(responseThemesDto: ResponseThemesDto): List<ThemesResponseEntity> {
        return responseThemesDto.meetThemeInfos.map { meetThemeInfo ->
            meetThemeInfo.run {
                ThemesResponseEntity(this.meetThemeId, this.name)
            }
        }
    }

    fun mapperToRequestCreateMeetDto(createMeetResponseEntity: CreateMeetRequestEntity): RequestCreateMeetDto {
        return createMeetResponseEntity.run {
            RequestCreateMeetDto(
                this.meetName,
                this.meetDescription,
                this.meetThemeId,
                this.places,
                this.voteDateRange.run {
                    RequestCreateMeetDto.VoteDateRange(this.start, this.end)
                },
                this.voteTimeRange.run {
                    RequestCreateMeetDto.VoteTimeRange(this.start, this.end)
                },
                this.endVoteHour
            )
        }
    }

    fun mapperToCreateMeetResponseEntity(responseCreateMeetDto: ResponseCreateMeetDto): CreateMeetResponseEntity {
        return responseCreateMeetDto.run {
            CreateMeetResponseEntity(this.meetId)
        }
    }

    fun mapperToMeetsResponseEntity(responseMeetsDto: ResponseMeetsDto): List<MeetsResponseEntity> {
        return responseMeetsDto.entryInfo.map { entryInfo ->
            entryInfo.run {
                MeetsResponseEntity(
                    this.meetStatus,
                    this.meetInfo.run {
                        MeetsResponseEntity.MeetInfo(
                            this.userVote,
                            this.meetId,
                            this.startMeetTime,
                            this.placeName,
                            this.address
                        )
                    }
                )
            }
        }
    }

    fun mapperToMeetDetailResponseEntity(responseMeetDetailDto: ResponseMeetDetailDto): MeetDetailResponseEntity {
        return responseMeetDetailDto.run {
            MeetDetailResponseEntity(
                this.userMeetRole,
                this.meetInfo.run {
                    MeetDetailResponseEntity.MeetInfo(
                        this.meetStatus,
                        this.meetId,
                        this.meetThemeName,
                        this.meetName,
                        this.meetDescription,
                        this.leftInviteTime
                    )
                },
                this.participantInfo.map { participant ->
                    participant.run {
                        MeetDetailResponseEntity.ParticipantInfo(
                            this.meetRole,
                            this.entryId,
                            this.imageUrl
                        )
                    }
                }
            )
        }
    }
}