package com.yomo.domain.model.response

data class VotePlaceResponseEntity(
    val meetStatus: String,
    val placeInfos: List<PlaceInfos>?,
) {
    data class PlaceInfos(
        val placeSlotId: Int,
        val title: String,
        val roadAddress: String,
        val mapX: String,
        val mapY: String,
        var isSelected: Boolean = false
    )

    fun selectFirstPlace(): List<PlaceInfos> = placeInfos?.mapIndexed { index, item ->
        item.copy(isSelected = index == 0)
    } ?: emptyList()
}