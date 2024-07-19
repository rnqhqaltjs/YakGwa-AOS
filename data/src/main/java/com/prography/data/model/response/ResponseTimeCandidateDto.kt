package com.prography.data.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseTimeCandidateDto(
    @SerializedName("meetStatus")
    val meetStatus: String,
    @SerializedName("timeInfos")
    val timeInfos: List<TimeInfo>?,
    @SerializedName("voteDate")
    val voteDate: VoteDate?
) {
    @Serializable
    data class TimeInfo(
        @SerializedName("timeId")
        val timeId: Int,
        @SerializedName("voteTime")
        val voteTime: String
    )

    @Serializable
    data class VoteDate(
        @SerializedName("startVoteDate")
        val startVoteDate: String,
        @SerializedName("endVoteDate")
        val endVoteDate: String,
    )
}