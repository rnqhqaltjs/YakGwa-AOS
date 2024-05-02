package com.prography.data.model.response.AuthResponse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("tokenSet")
    val tokenSet: TokenSet,
    @SerialName("userId")
    val userId: Int,
    @SerialName("isNew")
    val isNew: Boolean
)