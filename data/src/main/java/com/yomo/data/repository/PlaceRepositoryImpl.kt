package com.yomo.data.repository

import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.yomo.data.datasource.remote.PlaceRemoteDataSource
import com.yomo.data.mapper.PlaceMapper
import com.yomo.domain.model.request.MyPlaceRequestEntity
import com.yomo.domain.model.response.LocationResponseEntity
import com.yomo.domain.repository.PlaceRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeRemoteDataSource: PlaceRemoteDataSource,
    private val context: Context
) : PlaceRepository {
    override suspend fun getLocations(search: String): ApiResponse<Flow<List<LocationResponseEntity>>> {
        val response = placeRemoteDataSource.getLocations(search)

        return response.mapSuccess {
            flow {
                emit(PlaceMapper.mapperToLocationResponseEntity(result))
            }
        }
    }

    override suspend fun getMyPlace(): ApiResponse<Flow<List<LocationResponseEntity>>> {
        val response = placeRemoteDataSource.getMyPlace()

        return response.mapSuccess {
            flow {
                emit(PlaceMapper.mapperToLocationResponseEntity(result))
            }
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

    override suspend fun geoCoding(address: String): Location {
        return withContext(Dispatchers.IO) {
            try {
                Geocoder(context, Locale.KOREA).getFromLocationName(address, 1)?.let {
                    Location("").apply {
                        latitude = it[0].latitude
                        longitude = it[0].longitude
                    }
                } ?: Location("").apply {
                    latitude = 0.0
                    longitude = 0.0
                }
            } catch (e: Exception) {
                e.printStackTrace()
                geoCoding(address)
            }
        }
    }
}
