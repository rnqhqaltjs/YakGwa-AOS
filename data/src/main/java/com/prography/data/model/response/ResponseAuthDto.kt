package com.prography.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAuthDto(
    @SerialName("tokenSet")
    val tokenSet: TokenSet,
    @SerialName("userId")
    val userId: Int,
    @SerialName("isNew")
    val isNew: Boolean
) {
    @Serializable
    data class TokenSet(
        @SerialName("accessToken")
        val accessToken: String,
        @SerialName("refreshToken")
        val refreshToken: String
    )
}