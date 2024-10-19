package com.yomo.domain.usecase

import com.yomo.domain.model.response.PromiseHistoryResponseEntity
import com.yomo.domain.repository.MeetRepository
import com.skydoves.sandwich.ApiResponse

class GetPromiseHistoryListUseCase(
    private val meetRepository: MeetRepository
) {
    suspend operator fun invoke(): ApiResponse<List<PromiseHistoryResponseEntity>> =
        meetRepository.getPromiseHistory()
}