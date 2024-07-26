package com.prography.data.remote

import com.prography.data.model.response.ResponseLocationDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverService {
    @Headers(
        "X-Naver-Client-Id: AcQAynjHAzd_bd13r9OJ",
        "X-Naver-Client-Secret: 7SAobvpYDG"
    )
    @GET("v1/search/local.json")
    suspend fun getLocations(
        @Query("query") query: String,
        @Query("display") display: Int
    ): ResponseLocationDto
}