package com.prography.data.repository

import com.prography.data.datasource.remote.NaverRemoteDataSource
import com.prography.data.mapper.NaverMapper
import com.prography.domain.model.response.LocationResponseEntity
import com.prography.domain.repository.NaverRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NaverRepositoryImpl @Inject constructor(
    private val naverRemoteDataSource: NaverRemoteDataSource
) : NaverRepository {
    override suspend fun getLocations(
        query: String
    ): Flow<List<LocationResponseEntity>> = flow {
        val result = runCatching {
            NaverMapper.mapperToLocationResponseEntity(
                naverRemoteDataSource.getLocations(
                    query,
                    LOCATION_DISPLAY_COUNT
                )
            )
        }
        emit(result.getOrThrow())
    }

    companion object {
        private const val LOCATION_DISPLAY_COUNT = 5
    }
}
