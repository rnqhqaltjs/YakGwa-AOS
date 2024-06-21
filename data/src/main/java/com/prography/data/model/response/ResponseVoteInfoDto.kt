package com.prography.data.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseVoteInfoDto(
    @SerializedName("voteStatus")
    val voteStatus: String,
    @SerializedName("myPlaceVoteInfo")
    val myPlaceVoteInfo: List<MyPlaceVoteInfo>,
    @SerializedName("myTimeVoteInfo")
    val myTimeVoteInfo: List<MyTimeVoteInfo>,
) {
    @Serializable
    data class MyPlaceVoteInfo(
        @SerializedName("name")
        val name: String,
        @SerializedName("address")
        val address: String
    )

    @Serializable
    data class MyTimeVoteInfo(
        @SerializedName("start")
        val start: String,
        @SerializedName("end")
        val end: String
    )
}