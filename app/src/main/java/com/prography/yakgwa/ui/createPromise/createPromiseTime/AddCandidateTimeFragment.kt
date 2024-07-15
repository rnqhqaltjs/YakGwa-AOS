package com.prography.yakgwa.ui.createPromise.createPromiseTime

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
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
            add(Calendar.MONTH, MAX_MONTH)
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
        updateSelectedDatesInCalendar()
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
            if (dates.size > MAX_SELECT_RANGE) {
                calendarView.clearSelection()
                viewModel.setSelectedDates(emptyList())
                Snackbar.make(requireView(), "최대 2주까지만 설정할 수 있어요.", Snackbar.LENGTH_SHORT).show()
            } else {
                viewModel.setSelectedDates(dates)
            }
        }

        calendarView.setOnDateChangedListener { _, date, selected ->
            if (!selected) {
                viewModel.setSelectedDates(emptyList())
            } else {
                viewModel.setSelectedDates(listOf(date))
            }
        }
    }

    private fun updateSelectedDatesInCalendar() {
        viewModel.selectedCalendarDates.value.forEach { date ->
            binding.calendarView.setDateSelected(date, true)
        }
    }

    override fun onResume() {
        super.onResume()
        updateSelectedDatesInCalendar()
    }

    private fun observer() {
    }

    private fun addListeners() {
    }

    companion object {
        private const val MONTH_OFFSET = 1
        private const val FIRST_DAY_OF_MONTH = 1
        private const val MAX_MONTH = 5
        private const val MAX_SELECT_RANGE = 14
    }
}