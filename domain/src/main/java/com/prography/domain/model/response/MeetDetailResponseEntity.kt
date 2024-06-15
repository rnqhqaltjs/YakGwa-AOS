package com.prography.domain.model.response

data class MeetDetailResponseEntity(
    val userMeetRole: String,
    val meetInfo: MeetInfo,
    val participantInfo: List<ParticipantInfo>,
) {
    data class MeetInfo(
        val meetStatus: String,
        val meetId: Int,
        val meetThemeName: String,
        val meetName: String,
        val meetDescription: String,
        val leftInviteTime: String,
    )

    data class ParticipantInfo(
        val meetRole: String,
        val entryId: Int,
        val imageUrl: String?,
    )
}