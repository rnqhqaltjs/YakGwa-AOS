package com.yomo.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestConfirmPlaceDto(
    @SerialName("confirmPlaceSlotId")
    val confirmPlaceSlotId: Int
)