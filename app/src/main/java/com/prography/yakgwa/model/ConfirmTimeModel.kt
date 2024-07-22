package com.prography.yakgwa.model

import com.prography.domain.model.response.TimeCandidateResponseEntity.TimeInfo

data class ConfirmTimeModel(
    val timeInfo: TimeInfo,
    var isSelected: Boolean = false
)
