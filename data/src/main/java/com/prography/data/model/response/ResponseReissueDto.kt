package com.prography.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseReissueDto(
    @SerialName("tokenSet")
    val tokenSet: TokenSet
) {
    @Serializable
    data class TokenSet(
        @SerialName("accessToken")
        val accessToken: String,
        @SerialName("refreshToken")
        val refreshToken: String
    )
}