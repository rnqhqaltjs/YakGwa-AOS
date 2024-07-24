package com.prography.domain.usecase

import com.prography.domain.model.Promise
import com.prography.domain.repository.PromiseRepository
import kotlinx.coroutines.flow.Flow

class GetPromiseHistoryListUseCase(
    private val promiseRepository: PromiseRepository
) {
    suspend operator fun invoke(): Flow<List<Promise>> =
        promiseRepository.getPromiseHistory()
}