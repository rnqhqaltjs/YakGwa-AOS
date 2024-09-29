package com.yomo.domain.repository

import android.graphics.Bitmap

interface NaverRepository {
    suspend fun getStaticMap(
        width: Int,
        height: Int,
        center: String,
        level: Int,
        markers: String
    ): Result<Bitmap>
}