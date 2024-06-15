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
        @SerializedName("meetStatus")
        val meetStatus: String,
        @SerializedName("meetInfo")
        val meetInfo: MeetInfo,
    )

    @Serializable
    data class MeetInfo(
        @SerializedName("userVote")
        val userVote: Boolean,
        @SerializedName("meetId")
        val meetId: Int,
        @SerializedName("startMeetTime")
        val startMeetTime: String?,
        @SerializedName("placeName")
        val placeName: String?,
        @SerializedName("address")
        val address: String?,
    )
}