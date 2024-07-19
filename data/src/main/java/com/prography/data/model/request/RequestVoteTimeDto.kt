package com.prography.data.model.request


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RequestVoteTimeDto(
    @SerializedName("enableTImes")
    val enableTimes: List<EnableTimes>
) {
    @Serializable
    data class EnableTimes(
        @SerializedName("enableTime")
        val enableTime: String
    )
}