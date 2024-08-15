package com.prography.domain.repository

import com.prography.domain.model.request.MyPlaceRequestEntity
import com.prography.domain.model.response.LocationResponseEntity
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    suspend fun getLocations(search: String): ApiResponse<Flow<List<LocationResponseEntity>>>
    suspend fun getMyPlace(): ApiResponse<List<LocationResponseEntity>>
    suspend fun myPlace(
        like: Boolean,
        myPlaceRequestEntity: MyPlaceRequestEntity
    ): ApiResponse<Boolean>
}