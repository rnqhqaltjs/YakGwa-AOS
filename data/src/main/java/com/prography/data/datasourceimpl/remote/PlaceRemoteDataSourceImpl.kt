package com.prography.data.datasourceimpl.remote

import com.prography.data.datasource.remote.PlaceRemoteDataSource
import com.prography.data.model.request.RequestMyPlaceDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseLocationDto
import com.prography.data.remote.PlaceService
import javax.inject.Inject

class PlaceRemoteDataSourceImpl @Inject constructor(
    private val placeService: PlaceService
) : PlaceRemoteDataSource {
    override suspend fun getLocations(search: String): BaseResponse<List<ResponseLocationDto>> {
        return placeService.getLocations(search)
    }

    override suspend fun getMyPlace(): BaseResponse<List<ResponseLocationDto>> {
        return placeService.getMyPlace()
    }

    override suspend fun myPlace(
        like: Boolean,
        requestMyPlaceDto: RequestMyPlaceDto
    ): BaseResponse<Boolean> {
        return placeService.myPlace(like, requestMyPlaceDto)
    }
}