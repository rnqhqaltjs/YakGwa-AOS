package com.prography.data.remote

import com.prography.data.model.request.RequestMyPlaceDto
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ResponseLocationDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PlaceService {
    @GET("/api/v1/search")
    suspend fun getLocations(
        @Query("search") search: String
    ): BaseResponse<List<ResponseLocationDto>>

    @GET("/api/v1/place/like")
    suspend fun getMyPlace(): BaseResponse<List<ResponseLocationDto>>

    @POST("/api/v1/place/like")
    suspend fun myPlace(
        @Query("like") like: Boolean,
        @Body requestMyPlaceDto: RequestMyPlaceDto
    ): BaseResponse<Boolean>
}