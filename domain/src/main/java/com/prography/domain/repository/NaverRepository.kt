package com.prography.domain.repository

import com.prography.domain.model.response.LocationResponseEntity
import kotlinx.coroutines.flow.Flow

interface NaverRepository {
    suspend fun getLocations(query: String): Flow<List<LocationResponseEntity>>
}