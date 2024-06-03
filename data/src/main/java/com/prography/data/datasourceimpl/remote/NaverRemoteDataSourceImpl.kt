package com.prography.data.datasourceimpl.remote

import com.prography.data.datasource.remote.NaverRemoteDataSource
import com.prography.data.model.response.ResponseLocationDto
import com.prography.data.service.NaverService
import javax.inject.Inject

class NaverRemoteDataSourceImpl @Inject constructor(
    private val naverService: NaverService
) : NaverRemoteDataSource {
    override suspend fun getLocations(query: String, display: Int): ResponseLocationDto {
        return naverService.getLocations(query, display)
    }
}