package com.prography.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestConfirmTimeDto(
    @SerialName("confirmTimeSlotId")
    val confirmTimeSlotId: Int
)