package com.prography.data.mapper

import com.prography.data.model.request.RequestCreateMeetDto
import com.prography.data.model.response.ResponseThemesDto
import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.ThemesResponseEntity

object MeetMapper {
    fun mapperToThemesResponseEntity(responseThemesDto: ResponseThemesDto): List<ThemesResponseEntity> {
        return responseThemesDto.meetThemeInfos.map { meetThemeInfo ->
            ThemesResponseEntity(meetThemeInfo.meetThemeId, meetThemeInfo.name)
        }
    }

    fun mapperToRequestCreateMeetDto(createMeetResponseEntity: CreateMeetRequestEntity): RequestCreateMeetDto {
        return createMeetResponseEntity.run {
            val voteDateRange =
                RequestCreateMeetDto.VoteDateRange(voteDateRange.start, voteDateRange.end)
            val voteTimeRange =
                RequestCreateMeetDto.VoteTimeRange(voteTimeRange.start, voteTimeRange.end)

            RequestCreateMeetDto(
                this.meetName,
                this.meetDescription,
                this.meetThemeId,
                this.placeDecide,
                this.places,
                this.timeDecide,
                voteDateRange,
                voteTimeRange
            )
        }
    }
}