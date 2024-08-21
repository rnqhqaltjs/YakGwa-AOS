package com.prography.data.datasource.remote

import okhttp3.ResponseBody

interface NaverRemoteDataSource {
    suspend fun getStaticMap(
        width: Int,
        height: Int,
        center: String,
        level: Int,
        markers: String
    ): ResponseBody
}