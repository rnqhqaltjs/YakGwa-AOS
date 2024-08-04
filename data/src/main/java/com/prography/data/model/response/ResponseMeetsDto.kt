package com.prography.data.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMeetsDto(
    @SerializedName("meetInfosWithStatus")
    val meetInfosWithStatus: List<MeetInfosWithStatus>
) {
    @Serializable
    data class MeetInfosWithStatus(
        @SerializedName("meetStatus")
        val meetStatus: String,
        @SerializedName("meetInfo")
        val meetInfo: MeetInfo,
    )

    @Serializable
    data class MeetInfo(
        @SerializedName("meetThemeName")
        val meetThemeName: String,
        @SerializedName("meetDateTime")
        val meetDateTime: String?,
        @SerializedName("placeName")
        val placeName: String?,
        @SerializedName("meetTitle")
        val meetTitle: String,
        @SerializedName("meetId")
        val meetId: Int,
        @SerializedName("description")
        val description: String?
    )
}