package com.prography.domain.usecase

import com.prography.domain.model.response.PromiseHistoryResponseEntity
import com.prography.domain.repository.MeetRepository

class GetPromiseHistoryListUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(): Result<List<PromiseHistoryResponseEntity>> =
        meetRepository.getPromiseHistory()
}