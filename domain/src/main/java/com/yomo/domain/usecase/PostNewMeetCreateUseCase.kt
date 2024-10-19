package com.yomo.domain.usecase

import com.yomo.domain.model.request.CreateMeetRequestEntity
import com.yomo.domain.model.response.CreateMeetResponseEntity
import com.yomo.domain.repository.MeetRepository
import com.skydoves.sandwich.ApiResponse

class PostNewMeetCreateUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(
        createMeetRequestEntity: CreateMeetRequestEntity
    ): ApiResponse<CreateMeetResponseEntity> =
        meetRepository.createMeet(createMeetRequestEntity)
}