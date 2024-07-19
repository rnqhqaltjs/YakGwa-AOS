package com.prography.yakgwa.util.dateTimeUtils

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val timeFormatter12Hour = DateTimeFormatter.ofPattern("a hh:mm")
    private val timeFormatter24Hour = DateTimeFormatter.ofPattern("HH:mm:ss")
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun formatDateToString(year: Int, month: Int, day: Int): String {
        val date = LocalDate.of(year, month, day)
        return date.format(dateFormatter)
    }

    fun parseDateFromString(dateString: String?): LocalDate {
        return dateString?.let {
            LocalDate.parse(it, dateFormatter)
        } ?: LocalDate.now()
    }

    fun formatTimeToString(hour: Int, minute: Int): String {
        val time = LocalTime.of(hour, minute)
        return time.format(timeFormatter12Hour)
    }

    fun parseTimeFromString(timeString: String): LocalTime {
        return LocalTime.parse(timeString, timeFormatter12Hour)
    }

    fun formatTimeTo24Hour(timeString: String): String {
        val localTime = LocalTime.parse(timeString, timeFormatter12Hour)
        return localTime.format(timeFormatter24Hour)
    }

    fun parseHourFromTimeString(timeString: String): Int {
        val localTime = LocalTime.parse(timeString, timeFormatter24Hour)
        return localTime.hour
    }

    fun formatLocalDateTimeToString(date: LocalDate, time: LocalTime): String {
        val dateTime = LocalDateTime.of(date, time)
        return dateTime.format(dateTimeFormatter)
    }

    fun LocalDate.toCalendarDay(dayOfMonth: Int = this.dayOfMonth): CalendarDay {
        return CalendarDay.from(
            this.year,
            this.monthValue - 1,
            dayOfMonth
        )
    }
}