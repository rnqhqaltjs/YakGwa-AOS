package com.prography.data.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMeetsDto(
    @SerializedName("entryInfo")
    val entryInfo: List<EntryInfo>
) {
    @Serializable
    data class EntryInfo(
        @SerializedName("meetInfo")
        val meetInfo: MeetInfo,
        @SerializedName("meetStatus")
        val meetStatus: String,
        @SerializedName("participantsInfo")
        val participantsInfo: List<ParticipantsInfo>
    )

    @Serializable
    data class MeetInfo(
        @SerializedName("address")
        val address: String,
        @SerializedName("meetId")
        val meetId: Int,
        @SerializedName("placeName")
        val placeName: String,
        @SerializedName("startMeetTime")
        val startMeetTime: String
    )

    @Serializable
    data class ParticipantsInfo(
        @SerializedName("imageUrl")
        val imageUrl: String,
        @SerializedName("participantId")
        val participantId: Int
    )
}