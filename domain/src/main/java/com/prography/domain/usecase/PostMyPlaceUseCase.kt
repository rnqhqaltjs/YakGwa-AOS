package com.prography.domain.usecase

import com.prography.domain.model.request.MyPlaceRequestEntity
import com.prography.domain.repository.PlaceRepository
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