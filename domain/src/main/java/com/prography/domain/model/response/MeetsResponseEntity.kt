package com.prography.domain.model.response

data class MeetsResponseEntity(
    val meetStatus: String,
    val meetInfo: MeetInfo
) {
    data class MeetInfo(
        val userVote: Boolean,
        val meetId: Int,
        val startMeetTime: String?,
        val placeName: String?,
        val address: String?,
    )
}