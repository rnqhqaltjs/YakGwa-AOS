package com.prography.domain.usecase

import com.prography.domain.model.response.PromiseHistoryResponseEntity
import com.prography.domain.repository.MeetRepository
import com.skydoves.sandwich.ApiResponse

class GetPromiseHistoryListUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(): ApiResponse<List<PromiseHistoryResponseEntity>> =
        meetRepository.getPromiseHistory()
}