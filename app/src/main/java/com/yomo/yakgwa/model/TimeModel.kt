package com.yomo.yakgwa.model

import java.time.LocalTime

data class TimeModel(
    val time: LocalTime,
    var voteCount: Int = 0,
    var isSelected: Boolean = false
)
