package com.yomo.yakgwa.util.calendarUtils

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.yomo.yakgwa.R
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter
import java.util.Calendar
import java.util.Locale

class WeekDayColorFormatter(private val context: Context) : WeekDayFormatter {
    override fun format(dayOfWeek: Int): CharSequence {
        val textColor = getTextColor(dayOfWeek)
        val displayName = getDisplayName(dayOfWeek)
        return applyColorToText(displayName!!, textColor)
    }

    private fun getTextColor(dayOfWeek: Int): Int {
        return when (dayOfWeek) {
            Calendar.SATURDAY -> ContextCompat.getColor(context, R.color.sub_blue)
            Calendar.SUNDAY -> ContextCompat.getColor(context, R.color.sub_red)
            else -> ContextCompat.getColor(context, android.R.color.black)
        }
    }

    private fun getDisplayName(dayOfWeek: Int): String? {
        return Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
        }.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
    }

    private fun applyColorToText(text: CharSequence, color: Int): Spannable {
        return SpannableString(text).apply {
            setSpan(
                ForegroundColorSpan(color),
                0,
                length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}