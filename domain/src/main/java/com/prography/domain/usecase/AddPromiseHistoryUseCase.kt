package com.prography.domain.usecase

import com.prography.domain.model.Promise
import com.prography.domain.repository.PromiseRepository

class AddPromiseHistoryUseCase(
    private val promiseRepository: PromiseRepository
) {
    suspend operator fun invoke(promise: Promise): Unit =
        promiseRepository.addPromise(promise)
}