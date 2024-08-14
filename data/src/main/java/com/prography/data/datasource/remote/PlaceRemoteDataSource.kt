package com.prography.data.datasource.remote

import com.prography.data.model.request.RequestMyPlaceDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseLocationDto
import com.skydoves.sandwich.ApiResponse

interface PlaceRemoteDataSource {
    suspend fun getLocations(search: String): ApiResponse<BaseResponse<List<ResponseLocationDto>>>
    suspend fun getMyPlace(): ApiResponse<BaseResponse<List<ResponseLocationDto>>>
    suspend fun myPlace(
        like: Boolean,
        requestMyPlaceDto: RequestMyPlaceDto
    ): ApiResponse<BaseResponse<Boolean>>
}