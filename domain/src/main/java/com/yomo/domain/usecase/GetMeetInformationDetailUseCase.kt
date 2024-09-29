package com.yomo.domain.usecase

import com.yomo.domain.model.response.MeetDetailResponseEntity
import com.yomo.domain.repository.MeetRepository
import com.skydoves.sandwich.ApiResponse

class GetMeetInformationDetailUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(meetId: Int): ApiResponse<MeetDetailResponseEntity> =
        meetRepository.getMeetInformationDetail(meetId)
}