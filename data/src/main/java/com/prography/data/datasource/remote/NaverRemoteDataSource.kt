package com.prography.data.datasource.remote

import com.prography.data.model.response.ResponseLocationDto

interface NaverRemoteDataSource {
    suspend fun getLocations(query: String, display: Int): ResponseLocationDto
}