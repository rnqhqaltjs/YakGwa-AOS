package com.yomo.data.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseThemesDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

