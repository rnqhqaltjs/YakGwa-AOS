package com.prography.yakgwa.model

import com.prography.domain.model.response.VotePlaceResponseEntity.PlaceInfos

data class ConfirmPlaceModel(
    val placeInfos: PlaceInfos,
    var isSelected: Boolean = false
)
