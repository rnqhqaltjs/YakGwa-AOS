package com.yomo.domain.usecase

import com.yomo.domain.model.response.LocationResponseEntity
import com.yomo.domain.repository.PlaceRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.getOrElse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class GetLocationListUseCase(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(search: String): ApiResponse<Flow<List<LocationResponseEntity>>> {
        val locationResponse =
            placeRepository.getLocations(search).getOrElse { flowOf(emptyList()) }
        val myPlaceResponse = placeRepository.getMyPlace().getOrElse { flowOf(emptyList()) }

        val combinedFlow: Flow<List<LocationResponseEntity>> = combine(
            locationResponse,
            myPlaceResponse
        ) { locationList, myPlaceList ->
            (myPlaceList + locationList).distinctBy { it.placeInfoEntity.title }
        }.flowOn(Dispatchers.IO)

        return ApiResponse.Success(combinedFlow)
    }
}