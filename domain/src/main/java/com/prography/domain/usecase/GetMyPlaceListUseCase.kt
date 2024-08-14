package com.prography.domain.usecase

import com.prography.domain.model.response.LocationResponseEntity
import com.prography.domain.repository.PlaceRepository
import com.skydoves.sandwich.ApiResponse

class GetMyPlaceListUseCase(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(): ApiResponse<List<LocationResponseEntity>> =
        placeRepository.getMyPlace()
}