package com.prography.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseParticipantMeetDto(
    @SerialName("participantId")
    val participantId: Int
)