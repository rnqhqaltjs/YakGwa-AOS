package com.prography.data.datasourceimpl.remote

import com.prography.data.datasource.remote.NaverRemoteDataSource
import com.prography.data.remote.NaverService
import okhttp3.ResponseBody
import javax.inject.Inject

class NaverRemoteDataSourceImpl @Inject constructor(
    private val naverService: NaverService
) : NaverRemoteDataSource {
    override suspend fun getStaticMap(
        width: Int,
        height: Int,
        center: String,
        level: Int,
        markers: String
    ): ResponseBody {
        return naverService.getStaticMap(width, height, center, level, markers)
    }
}