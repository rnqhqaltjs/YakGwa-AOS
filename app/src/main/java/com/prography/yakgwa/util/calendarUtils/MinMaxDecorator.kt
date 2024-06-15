package com.prography.yakgwa.util.calendarUtils

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class MinMaxDecorator(private val minDay: CalendarDay, private val maxDay: CalendarDay) :
    DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return (day?.month == maxDay.month && day.day > maxDay.day)
                || (day?.month == minDay.month && day.day < minDay.day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object : ForegroundColorSpan(Color.parseColor("#d2d2d2")) {})
        view?.setDaysDisabled(true)
    }
}