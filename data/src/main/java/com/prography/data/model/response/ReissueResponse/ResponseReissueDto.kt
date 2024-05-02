package com.prography.data.model.response.ReissueResponse


import com.google.gson.annotations.SerializedName
import com.prography.data.model.response.AuthResponse.Result
import com.prography.data.model.response.AuthResponse.TokenSet

data class ResponseReissueDto(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: Result,
    @SerializedName("status")
    val status: Int,
    @SerializedName("time")
    val time: String
)