package com.yomo.domain.usecase

import com.yomo.domain.model.Promise
import com.yomo.domain.repository.PromiseRepository

class AddPromiseHistoryUseCase(
    private val promiseRepository: PromiseRepository
) {
    suspend operator fun invoke(promise: Promise): Unit =
        promiseRepository.addPromise(promise)
}