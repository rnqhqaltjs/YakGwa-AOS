package com.prography.yakgwa.util.calendarUtils

import android.content.Context
import android.os.Build
import android.text.style.ForegroundColorSpan
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.prography.yakgwa.R
import com.prography.yakgwa.model.TimeModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.time.LocalDate

class TimeSelectedDecorator(
    private val context: Context,
    private val timeSlots: Map<LocalDate, List<TimeModel>>,
    private val selectedDate: LocalDate?
) : DayViewDecorator {

    @RequiresApi(Build.VERSION_CODES.O)
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