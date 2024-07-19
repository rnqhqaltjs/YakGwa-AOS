package com.prography.data.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseVotePlaceDto(
    @SerializedName("meetStatus")
    val meetStatus: String,
    @SerializedName("placeInfos")
    val placeInfos: List<PlaceInfos>?,
) {
    @Serializable
    data class PlaceInfos(
        @SerializedName("placeId")
        val placeId: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("roadAddress")
        val roadAddress: String,
        @SerializedName("mapx")
        val mapx: String,
        @SerializedName("mapy")
        val mapy: String,
    )
}