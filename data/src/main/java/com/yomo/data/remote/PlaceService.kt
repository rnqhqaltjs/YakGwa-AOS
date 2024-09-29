package com.yomo.data.remote

import com.yomo.data.model.request.RequestMyPlaceDto
import com.yomo.data.model.response.BaseResponse
import com.yomo.data.model.response.ResponseLocationDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PlaceService {
    @GET("/api/v1/search")
    suspend fun getLocations(
        @Query("search") search: String
    ): ApiResponse<BaseResponse<List<ResponseLocationDto>>>

    @GET("/api/v1/place/like")
    suspend fun getMyPlace(): ApiResponse<BaseResponse<List<ResponseLocationDto>>>

    @POST("/api/v1/place/like")
    suspend fun myPlace(
        @Query("like") like: Boolean,
        @Body requestMyPlaceDto: RequestMyPlaceDto
    ): ApiResponse<BaseResponse<Boolean>>
}