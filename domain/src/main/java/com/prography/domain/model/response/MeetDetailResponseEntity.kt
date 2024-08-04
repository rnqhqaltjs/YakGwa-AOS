package com.prography.domain.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class MeetDetailResponseEntity(
    val meetInfo: MeetInfo,
    val participantInfo: List<ParticipantInfo>,
) {
    data class MeetInfo(
        val meetTitle: String,
        val description: String,
        val themeName: String,
    )

    @Parcelize
    data class ParticipantInfo(
        val meetRole: String,
        val imageUrl: String?,
        val name: String
    ) : Parcelable
}