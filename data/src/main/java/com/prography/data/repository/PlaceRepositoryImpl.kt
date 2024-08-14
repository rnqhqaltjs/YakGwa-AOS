package com.prography.data.repository

import com.prography.data.datasource.remote.PlaceRemoteDataSource
import com.prography.data.mapper.PlaceMapper
import com.prography.domain.model.request.MyPlaceRequestEntity
import com.prography.domain.model.response.LocationResponseEntity
import com.prography.domain.repository.PlaceRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeRemoteDataSource: PlaceRemoteDataSource
) : PlaceRepository {
    override suspend fun getLocations(search: String): ApiResponse<Flow<List<LocationResponseEntity>>> {
        val response = placeRemoteDataSource.getLocations(search)

        return response.mapSuccess {
            flow {
                emit(PlaceMapper.mapperToLocationResponseEntity(result))
            }
        }
    }

    override suspend fun getMyPlace(): ApiResponse<List<LocationResponseEntity>> {
        val response = placeRemoteDataSource.getMyPlace()

        return response.mapSuccess {
            PlaceMapper.mapperToLocationResponseEntity(
                this.result
            )
        }
    }

    override suspend fun myPlace(
        like: Boolean,
        myPlaceRequestEntity: MyPlaceRequestEntity
    ): ApiResponse<Boolean> {
        val response = placeRemoteDataSource.myPlace(
            like,
            PlaceMapper.mapperToRequestMyPlaceDto(myPlaceRequestEntity)
        )
        return response.mapSuccess {
            this.result
        }
    }
}
