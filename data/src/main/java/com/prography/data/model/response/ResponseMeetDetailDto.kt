package com.prography.data.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMeetDetailDto(
    @SerializedName("userMeetRole")
    val userMeetRole: String,
    @SerializedName("meetInfo")
    val meetInfo: MeetInfo,
    @SerializedName("participantInfo")
    val participantInfo: List<ParticipantInfo>
) {
    @Serializable
    data class MeetInfo(
        @SerializedName("meetStatus")
        val meetStatus: String,
        @SerializedName("meetId")
        val meetId: Int,
        @SerializedName("meetThemeName")
        val meetThemeName: String,
        @SerializedName("meetName")
        val meetName: String,
        @SerializedName("meetDescription")
        val meetDescription: String,
        @SerializedName("leftInviteTime")
        val leftInviteTime: String,
    )

    @Serializable
    data class ParticipantInfo(
        @SerializedName("meetRole")
        val meetRole: String,
        @SerializedName("entryId")
        val entryId: Int,
        @SerializedName("imageUrl")
        val imageUrl: String?,
    )
}