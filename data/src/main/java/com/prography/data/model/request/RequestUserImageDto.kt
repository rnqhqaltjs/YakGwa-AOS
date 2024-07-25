package com.prography.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestUserImageDto(
    @SerialName("userImage")
    val userImage: String
)