package com.prography.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestMyPlaceDto(
    @SerialName("title")
    val title: String,
    @SerialName("mapx")
    val mapx: String,
    @SerialName("mapy")
    val mapy: String
)
