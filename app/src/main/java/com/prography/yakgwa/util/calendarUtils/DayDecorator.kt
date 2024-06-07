package com.prography.yakgwa.util.calendarUtils

import android.content.Context
import androidx.core.content.ContextCompat
import com.prography.yakgwa.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class DayDecorator(context: Context, min: CalendarDay, max: CalendarDay) : DayViewDecorator {
    private val drawable = ContextCompat.getDrawable(context, R.drawable.selector_calendar_day)

    private val maxDay = max
    private val minDay = min
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day ?: return false
        return day.isWithin(minDay, maxDay)
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable!!)
    }

    private fun CalendarDay.isWithin(min: CalendarDay, max: CalendarDay): Boolean {
        return (this == min || this == max || (this.isAfter(min) && this.isBefore(max)))
    }
}