package com.yomo.data.model.request


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RequestCreateMeetDto(
    @SerializedName("meetInfo")
    val meetInfo: MeetInfo
) {
    @Serializable
    data class MeetInfo(
        @SerializedName("meetTitle")
        val meetTitle: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("meetThemeId")
        val meetThemeId: Int,
        @SerializedName("confirmPlace")
        val confirmPlace: Boolean,
        @SerializedName("placeInfo")
        val placeInfo: List<PlaceInfo>,
        @SerializedName("voteDate")
        val voteDate: VoteDate?,
        @SerializedName("meetTime")
        val meetTime: String?,
    )

    @Serializable
    data class PlaceInfo(
        @SerializedName("title")
        val title: String,
        @SerializedName("link")
        val link: String,
        @SerializedName("category")
        val category: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("telephone")
        val telephone: String,
        @SerializedName("address")
        val address: String,
        @SerializedName("roadAddress")
        val roadAddress: String,
        @SerializedName("mapx")
        val mapx: String,
        @SerializedName("mapy")
        val mapy: String,
    )

    @Serializable
    data class VoteDate(
        @SerializedName("startVoteDate")
        val startVoteDate: String,
        @SerializedName("endVoteDate")
        val endVoteDate: String
    )
}