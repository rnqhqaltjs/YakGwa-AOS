package com.yomo.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAuthDto(
    @SerialName("tokenSet")
    val tokenSet: TokenSet,
    @SerialName("isNew")
    val isNew: Boolean,
    @SerialName("role")
    val role: String?
) {
    @Serializable
    data class TokenSet(
        @SerialName("accessToken")
        val accessToken: String,
        @SerialName("refreshToken")
        val refreshToken: String
    )
}