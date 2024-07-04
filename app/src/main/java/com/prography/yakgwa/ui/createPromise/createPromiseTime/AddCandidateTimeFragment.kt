package com.prography.yakgwa.ui.createPromise.createPromiseTime

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentAddCandidateTimeBinding
import com.prography.yakgwa.ui.createPromise.CreatePromiseViewModel
import com.prography.yakgwa.util.base.BaseFragment
import com.prography.yakgwa.util.calendarUtils.MinMaxDecorator
import com.prography.yakgwa.util.calendarUtils.SelectDayDecorator
import com.prography.yakgwa.util.calendarUtils.WeekDayColorFormatter
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.util.Calendar

@AndroidEntryPoint
class AddCandidateTimeFragment :
    BaseFragment<FragmentAddCandidateTimeBinding>(R.layout.fragment_add_candidate_time) {
    private val viewModel: CreatePromiseViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val (startDate, endDate) = calculateDateRange()
        initCalendarView(startDate, endDate)
        observer()
        addListeners()
    }

    private fun calculateDateRange(): Pair<CalendarDay, CalendarDay> {
        val today = Calendar.getInstance()
        val startDate = CalendarDay.from(today)

        val endOfMonth = Calendar.getInstance().apply {
            add(Calendar.MONTH, 5)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        }
        val endDate = CalendarDay.from(endOfMonth)

        return Pair(startDate, endDate)
    }

    private fun initCalendarView(
        startDate: CalendarDay,
        endDate: CalendarDay
    ) {
        binding.calendarView.apply {
            setupTitleFormatter(this)
            setupWeekdayFormatter(this)
            configureDateRange(this, startDate, endDate)
            applyCalendarDecorators(this, startDate, endDate)
            setupDateChangeListener(this)
        }
    }

    private fun applyCalendarDecorators(
        calendarView: MaterialCalendarView,
        startDate: CalendarDay,
        endDate: CalendarDay,
    ) {
        calendarView.apply {
            removeDecorators()
            invalidateDecorators()

            startDate.let { start ->
                endDate.let { end ->
                    addDecorators(
                        MinMaxDecorator(start, end),
                        SelectDayDecorator(requireContext(), start, end)
                    )
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun setupTitleFormatter(calendarView: MaterialCalendarView) {
        calendarView.setTitleFormatter { day ->
            "${day.year}.${String.format("%02d", day.month + MONTH_OFFSET)}"
        }
    }

    private fun setupWeekdayFormatter(calendarView: MaterialCalendarView) {
        calendarView.setWeekDayFormatter(WeekDayColorFormatter(requireContext()))
    }

    private fun configureDateRange(
        calendarView: MaterialCalendarView,
        startDate: CalendarDay,
        endDate: CalendarDay,
    ) {
        val firstDayOfMonth = CalendarDay.from(startDate.year, startDate.month, FIRST_DAY_OF_MONTH)

        calendarView.state().edit()
            .setMinimumDate(firstDayOfMonth)
            .setMaximumDate(endDate)
            .commit()
    }

    private fun setupDateChangeListener(calendarView: MaterialCalendarView) {
        calendarView.setOnRangeSelectedListener { _, dates ->
            val selectedDates = dates.map { date ->
                LocalDate.of(date.year, date.month + MONTH_OFFSET, date.day)
            }
            viewModel.setSelectedDates(selectedDates)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observer() {
    }

    private fun addListeners() {
    }

    companion object {
        private const val MONTH_OFFSET = 1
        private const val FIRST_DAY_OF_MONTH = 1
    }
}