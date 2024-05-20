package com.prography.data.model.response.ReissueResponse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseReissueDto(
    @SerialName("tokenSet")
    val tokenSet: TokenSet
)