package com.prography.yakgwa.model

import java.time.LocalDate
import java.time.LocalTime

data class DateTimeModel(
    val date: LocalDate,
    val time: LocalTime,
    var voteCount: Int = 0,
    var isSelected: Boolean = false
)
