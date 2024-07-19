package com.prography.yakgwa.model

import com.prography.domain.model.response.PlaceCandidateResponseEntity

data class PlaceModel(
    val placeItem: PlaceCandidateResponseEntity,
    var isSelected: Boolean = false
)
