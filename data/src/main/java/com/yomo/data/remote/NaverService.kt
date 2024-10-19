package com.yomo.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverService {
    @Headers(
        "X-NCP-APIGW-API-KEY-ID: ddmbap85ls",
        "X-NCP-APIGW-API-KEY: lJMQLJYNaLP7xmz3AW4t2GQsWUIq6nyuyuLzwPvn"
    )
    @GET("map-static/v2/raster")
    suspend fun getStaticMap(
        @Query("w") width: Int,
        @Query("h") height: Int,
        @Query("center", encoded = true) center: String,
        @Query("level") level: Int,
        @Query("markers", encoded = true) markers: String
    ): ResponseBody
}