package com.prography.domain.model.response

data class VotePlaceResponseEntity(
    val meetStatus: String,
    val placeInfos: List<PlaceInfos>?,
) {
    data class PlaceInfos(
        val placeId: Int,
        val title: String,
        val roadAddress: String,
        val mapX: String,
        val mapY: String,
    )
}