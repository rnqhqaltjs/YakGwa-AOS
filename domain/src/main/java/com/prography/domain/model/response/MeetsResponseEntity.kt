package com.prography.domain.model.response

data class MeetsResponseEntity(
    val meetInfo: MeetInfo,
    val meetStatus: String,
    val participantsInfo: List<ParticipantsInfo>
) {
    data class MeetInfo(
        val address: String?,
        val meetId: Int?,
        val placeName: String?,
        val startMeetTime: String?
    )

    data class ParticipantsInfo(
        val imageUrl: String,
        val participantId: Int
    )
}
