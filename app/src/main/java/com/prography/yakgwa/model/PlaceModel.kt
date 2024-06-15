package com.prography.yakgwa.model

import com.prography.domain.model.response.TimePlaceResponseEntity.PlaceItem

data class PlaceModel(
    val placeItem: PlaceItem,
    var isSelected: Boolean = false
)
