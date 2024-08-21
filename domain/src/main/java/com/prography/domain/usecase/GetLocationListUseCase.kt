package com.prography.domain.usecase

import com.prography.domain.model.response.LocationResponseEntity
import com.prography.domain.repository.PlaceRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.getOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetLocationListUseCase(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(search: String): ApiResponse<Flow<List<LocationResponseEntity>>> {
        val locationResponse = placeRepository.getLocations(search).getOrNull()
        val myPlaceResponse = placeRepository.getMyPlace().getOrNull()

        val combinedFlow: Flow<List<LocationResponseEntity>> = combine(
            locationResponse ?: flowOf(emptyList()),
            myPlaceResponse ?: flowOf(emptyList())
        ) { locationList, myPlaceList ->
            val combinedList = mutableListOf<LocationResponseEntity>()

            combinedList.addAll(myPlaceList)
            combinedList.addAll(locationList)
            combinedList
        }.map { list ->
            list.distinctBy { it.placeInfoEntity.title }
        }.flowOn(Dispatchers.IO)

        return ApiResponse.Success(combinedFlow)
    }
}