package com.yomo.domain.usecase

import com.yomo.domain.model.request.MyPlaceRequestEntity
import com.yomo.domain.repository.PlaceRepository
import com.skydoves.sandwich.ApiResponse

class PostMyPlaceUseCase(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(
        like: Boolean,
        myPlaceRequestEntity: MyPlaceRequestEntity
    ): ApiResponse<Boolean> =
        placeRepository.myPlace(like, myPlaceRequestEntity)
}