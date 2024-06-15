package com.prography.domain.usecase

import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.domain.repository.MeetRepository

class PostNewMeetCreateUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(
        userId: Int,
        createMeetRequestEntity: CreateMeetRequestEntity
    ): Result<CreateMeetResponseEntity> =
        meetRepository.createMeet(userId, createMeetRequestEntity)
}