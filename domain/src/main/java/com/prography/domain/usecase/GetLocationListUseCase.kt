package com.prography.domain.usecase

import com.prography.domain.model.response.LocationResponseEntity
import com.prography.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow

class GetLocationListUseCase(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(search: String): Flow<List<LocationResponseEntity>> =
        placeRepository.getLocations(search)
}