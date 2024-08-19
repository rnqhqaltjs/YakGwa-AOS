package com.prography.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.prography.data.datasource.remote.NaverRemoteDataSource
import com.prography.domain.repository.NaverRepository
import javax.inject.Inject

class NaverRepositoryImpl @Inject constructor(
    private val naverRemoteDataSource: NaverRemoteDataSource
) : NaverRepository {
    override suspend fun getStaticMap(
        width: Int,
        height: Int,
        center: String,
        level: Int,
        markers: String
    ): Result<Bitmap> {
        val response = naverRemoteDataSource.getStaticMap(width, height, center, level, markers)

        return runCatching {
            BitmapFactory.decodeStream(response.byteStream())
        }
    }
}
