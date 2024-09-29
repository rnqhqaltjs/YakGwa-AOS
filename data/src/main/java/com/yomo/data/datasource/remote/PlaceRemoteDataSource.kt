package com.yomo.data.datasource.remote

import com.yomo.data.model.request.RequestMyPlaceDto
import com.yomo.data.model.response.BaseResponse
import com.yomo.data.model.response.ResponseLocationDto
import com.skydoves.sandwich.ApiResponse

interface PlaceRemoteDataSource {
    suspend fun getLocations(search: String): ApiResponse<BaseResponse<List<ResponseLocationDto>>>
    suspend fun getMyPlace(): ApiResponse<BaseResponse<List<ResponseLocationDto>>>
    suspend fun myPlace(
        like: Boolean,
        requestMyPlaceDto: RequestMyPlaceDto
    ): ApiResponse<BaseResponse<Boolean>>
}