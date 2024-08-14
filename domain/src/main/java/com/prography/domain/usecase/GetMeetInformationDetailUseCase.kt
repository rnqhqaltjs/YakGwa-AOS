package com.prography.domain.usecase

import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.repository.MeetRepository
import com.skydoves.sandwich.ApiResponse

class GetMeetInformationDetailUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(meetId: Int): ApiResponse<MeetDetailResponseEntity> =
        meetRepository.getMeetInformationDetail(meetId)
}