package com.prography.domain.model.response

data class MeetDetailResponseEntity(
    val meetInfo: MeetInfo,
    val participantInfo: List<ParticipantInfo>,
) {
    data class MeetInfo(
        val meetTitle: String,
        val themeName: String,
    )

    data class ParticipantInfo(
        val meetRole: String,
        val imageUrl: String?,
        val participantId: Int,
    )
}