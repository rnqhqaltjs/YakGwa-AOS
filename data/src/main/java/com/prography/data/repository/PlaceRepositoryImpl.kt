package com.prography.data.repository

import com.prography.data.datasource.remote.PlaceRemoteDataSource
import com.prography.data.mapper.PlaceMapper
import com.prography.domain.model.request.MyPlaceRequestEntity
import com.prography.domain.model.response.LocationResponseEntity
import com.prography.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeRemoteDataSource: PlaceRemoteDataSource
) : PlaceRepository {
    override suspend fun getLocations(search: String): Flow<List<LocationResponseEntity>> = flow {
        val result = runCatching {
            PlaceMapper.mapperToLocationResponseEntity(placeRemoteDataSource.getLocations(search).result)
        }

        emit(result.getOrThrow())
    }

    override suspend fun getMyPlace(): Result<List<LocationResponseEntity>> {
        val response = placeRemoteDataSource.getMyPlace()

        return runCatching {
            PlaceMapper.mapperToLocationResponseEntity(
                response.result
            )
        }
    }

    override suspend fun myPlace(
        like: Boolean,
        myPlaceRequestEntity: MyPlaceRequestEntity
    ): Result<Boolean> {
        val response = placeRemoteDataSource.myPlace(
            like,
            PlaceMapper.mapperToRequestMyPlaceDto(myPlaceRequestEntity)
        )

        return runCatching {
            response.result
        }
    }
}
