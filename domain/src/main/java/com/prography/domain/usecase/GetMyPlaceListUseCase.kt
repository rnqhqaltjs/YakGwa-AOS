package com.prography.domain.usecase

import com.prography.domain.model.response.LocationResponseEntity
import com.prography.domain.repository.PlaceRepository

class GetMyPlaceListUseCase(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(): Result<List<LocationResponseEntity>> =
        placeRepository.getMyPlace()
}