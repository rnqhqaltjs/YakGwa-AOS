package com.prography.yakgwa.model

import com.prography.domain.model.response.LocationResponseEntity

data class SelectedLocationModel(
    val locationResponseEntity: LocationResponseEntity,
    var isSelected: Boolean = false
)
