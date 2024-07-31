package com.prography.domain.usecase

import com.prography.domain.model.request.MyPlaceRequestEntity
import com.prography.domain.repository.PlaceRepository

class PostMyPlaceUseCase(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(
        like: Boolean,
        myPlaceRequestEntity: MyPlaceRequestEntity
    ): Result<Boolean> =
        placeRepository.myPlace(like, myPlaceRequestEntity)
}