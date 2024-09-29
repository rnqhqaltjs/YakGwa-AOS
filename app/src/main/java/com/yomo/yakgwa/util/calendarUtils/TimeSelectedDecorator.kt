package com.yomo.yakgwa.util.calendarUtils

import android.content.Context
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.yomo.yakgwa.R
import com.yomo.yakgwa.model.TimeModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.time.LocalDate

class TimeSelectedDecorator(
    private val context: Context,
    private val timeSlots: Map<LocalDate, List<TimeModel>>,
    private val selectedDate: LocalDate?
) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        if (day == null) return false
        val localDate = LocalDate.of(day.year, day.month + 1, day.day)
        return timeSlots.any { it.key == localDate && it.value.any { slot -> slot.isSelected } } &&
                selectedDate != localDate
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object :
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.primary_700)) {})
    }
}