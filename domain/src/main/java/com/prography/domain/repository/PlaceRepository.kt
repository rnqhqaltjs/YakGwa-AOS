package com.prography.domain.repository

import com.prography.domain.model.request.MyPlaceRequestEntity
import com.prography.domain.model.response.LocationResponseEntity
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    suspend fun getLocations(search: String): Flow<List<LocationResponseEntity>>
    suspend fun getMyPlace(): Result<List<LocationResponseEntity>>
    suspend fun myPlace(
        like: Boolean,
        myPlaceRequestEntity: MyPlaceRequestEntity
    ): Result<Boolean>
}