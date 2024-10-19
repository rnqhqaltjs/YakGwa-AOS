package com.yomo.domain.usecase

import android.graphics.Bitmap
import com.yomo.domain.repository.NaverRepository

class GetStaticMapUseCase(
    private val naverRepository: NaverRepository
) {
    suspend operator fun invoke(
        width: Int,
        height: Int,
        center: String,
        level: Int,
        markers: String
    ): Result<Bitmap> =
        naverRepository.getStaticMap(width, height, center, level, markers)
}