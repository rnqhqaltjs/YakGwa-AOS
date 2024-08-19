package com.prography.domain.model.response

data class LocationResponseEntity(
    val placeInfoEntity: PlaceInfoEntity,
    val isUserLike: Boolean,
    var isSelected: Boolean = false
) {
    data class PlaceInfoEntity(
        val title: String,
        val link: String,
        val category: String,
        val description: String,
        val telephone: String,
        val address: String,
        val roadAddress: String,
        val mapx: String,
        val mapy: String
    )
}