package com.prography.data.model.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseLocationDto(
    @SerializedName("placeInfoDto")
    val placeInfoDto: PlaceInfoDto,
    @SerializedName("isUserLike")
    val isUserLike: Boolean
) {
    @Serializable
    data class PlaceInfoDto(
        @SerializedName("title")
        val title: String,
        @SerializedName("link")
        val link: String,
        @SerializedName("category")
        val category: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("telephone")
        val telephone: String,
        @SerializedName("address")
        val address: String,
        @SerializedName("roadAddress")
        val roadAddress: String,
        @SerializedName("mapx")
        val mapx: String,
        @SerializedName("mapy")
        val mapy: String
    )
}