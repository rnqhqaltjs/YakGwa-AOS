package com.yomo.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserInfoDto(
    @SerialName("name")
    val name: String,
    @SerialName("imageUrl")
    val imageUrl: String
)