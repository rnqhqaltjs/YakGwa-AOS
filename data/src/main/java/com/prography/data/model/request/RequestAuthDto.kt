package com.prography.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestAuthDto(
    @SerialName("loginType")
    val loginType: String,
    @SerialName("fcmToken")
    val fcmToken: String
)