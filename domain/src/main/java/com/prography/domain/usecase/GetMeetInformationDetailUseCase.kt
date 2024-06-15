package com.prography.domain.usecase

import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.repository.MeetRepository
import kotlinx.coroutines.flow.Flow

class GetMeetInformationDetailUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(userId: Int, meetId: Int): Flow<MeetDetailResponseEntity> =
        meetRepository.getMeetInformationDetail(userId, meetId)
}