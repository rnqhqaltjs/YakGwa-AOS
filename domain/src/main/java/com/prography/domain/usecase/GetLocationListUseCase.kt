package com.prography.domain.usecase

import com.prography.domain.model.response.LocationResponseEntity
import com.prography.domain.repository.NaverRepository
import kotlinx.coroutines.flow.Flow

class GetLocationListUseCase(
    private val naverRepository: NaverRepository
) {
    suspend operator fun invoke(query: String): Flow<List<LocationResponseEntity>> =
        naverRepository.getLocations(query)
}