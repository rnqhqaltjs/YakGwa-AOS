package com.prography.yakgwa.util.calendarUtils

import android.content.Context
import androidx.core.content.ContextCompat
import com.prography.yakgwa.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class MultiSelectDayDecorator(
    private val context: Context,
    private val selectedDays: Collection<CalendarDay>
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        if (day == null || selectedDays.isEmpty()) {
            return false
        }

        return day != selectedDays.first() && day != selectedDays.last()
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.selector_range_calendar_day
            )!!
        )
    }

}