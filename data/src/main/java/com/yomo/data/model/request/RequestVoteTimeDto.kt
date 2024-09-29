package com.yomo.data.model.request


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RequestVoteTimeDto(
    @SerializedName("enableTimes")
    val enableTimes: List<EnableTimes>
) {
    @Serializable
    data class EnableTimes(
        @SerializedName("enableTime")
        val enableTime: String
    )
}