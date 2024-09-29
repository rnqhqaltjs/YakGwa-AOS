package com.yomo.data.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMeetDetailDto(
    @SerializedName("meetInfo")
    val meetInfo: MeetInfo,
    @SerializedName("participantInfo")
    val participantInfo: List<ParticipantInfo>
) {
    @Serializable
    data class MeetInfo(
        @SerializedName("meetTitle")
        val meetTitle: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("themeName")
        val themeName: String
    )

    @Serializable
    data class ParticipantInfo(
        @SerializedName("meetRole")
        val meetRole: String,
        @SerializedName("imageUrl")
        val imageUrl: String?,
        @SerializedName("name")
        val name: String,
    )
}