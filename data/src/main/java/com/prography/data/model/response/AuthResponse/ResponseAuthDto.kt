package com.prography.data.model.response.AuthResponse


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAuthDto(
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: Result,
    @SerialName("status")
    val status: Int,
    @SerialName("time")
    val time: String
)