package com.prography.yakgwa.util.calendarUtils

import android.content.Context
import androidx.core.content.ContextCompat
import com.prography.yakgwa.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class SelectDayDecorator(
    private val context: Context,
    private val minDay: CalendarDay,
    private val maxDay: CalendarDay
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day ?: return false
        return day.isWithin(minDay, maxDay)
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.selector_calendar_day
            )!!
        )
    }

    private fun CalendarDay.isWithin(min: CalendarDay, max: CalendarDay): Boolean {
        return (this == min || this == max || (this.isAfter(min) && this.isBefore(max)))
    }
}