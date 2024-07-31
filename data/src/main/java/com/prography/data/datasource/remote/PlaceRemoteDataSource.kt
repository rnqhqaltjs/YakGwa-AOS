package com.prography.data.datasource.remote

import com.prography.data.model.request.RequestMyPlaceDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseLocationDto

interface PlaceRemoteDataSource {
    suspend fun getLocations(search: String): BaseResponse<List<ResponseLocationDto>>
    suspend fun getMyPlace(): BaseResponse<List<ResponseLocationDto>>
    suspend fun myPlace(
        like: Boolean,
        requestMyPlaceDto: RequestMyPlaceDto
    ): BaseResponse<Boolean>
}