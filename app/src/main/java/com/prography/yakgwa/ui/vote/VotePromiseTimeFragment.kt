package com.prography.yakgwa.ui.vote

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentVotePromiseTimeBinding
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import com.prography.yakgwa.util.calendarUtils.DayDecorator
import com.prography.yakgwa.util.calendarUtils.MinMaxDecorator
import com.prography.yakgwa.util.calendarUtils.WeekDayColorFormatter
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate


@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DefaultLocale")
class VotePromiseTimeFragment :
    BaseFragment<FragmentVotePromiseTimeBinding>(R.layout.fragment_vote_promise_time) {

    private val viewModel: VoteViewModel by viewModels()

    private lateinit var timeListAdapter: TimeListAdapter
    private val args by navArgs<VotePromiseTimeFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val meetId = args.meetId

        initView(meetId)
        observer()
        addListeners(meetId)
    }

    private fun initView(meetId: Int) {
        viewModel.getTimePlaceCandidate(meetId)
    }

    private fun initCalendarView() {
        binding.calendarView.apply {
            configureCalendarView(this)
            setupCalendarDecorators(this)
            setupTitleFormatter(this)
            setupWeekdayFormatter(this)
            configureDateRange(this)
            setupDateChangeListener(this)
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.timePlaceState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            initCalendarView()
                            setupRecyclerView()
                        }

                        is UiState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun addListeners(meetId: Int) {
        binding.btnVoteNext.setOnClickListener {
            navigateToVotePromisePlaceFragment(meetId)
        }

        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        timeListAdapter = TimeListAdapter().apply {
            setOnItemClickListener {

            }
        }
        binding.rvTime.adapter = timeListAdapter
    }

    private fun configureCalendarView(calendarView: MaterialCalendarView) {
        calendarView.setTopbarVisible(false)
        calendarView.isDynamicHeightEnabled = true
    }

    private fun setupCalendarDecorators(calendarView: MaterialCalendarView) {
        val startDate = LocalDate.parse(viewModel.startDateState.value)
        val endDate = LocalDate.parse(viewModel.endDateState.value)

        val startCalendarDay =
            CalendarDay(startDate.year, startDate.monthValue - 1, startDate.dayOfMonth)
        val endCalendarDay = CalendarDay(endDate.year, endDate.monthValue - 1, endDate.dayOfMonth)

        calendarView.addDecorators(
            MinMaxDecorator(startCalendarDay, endCalendarDay),
            DayDecorator(requireContext(), startCalendarDay, endCalendarDay)
        )
    }

    private fun setupTitleFormatter(calendarView: MaterialCalendarView) {
        calendarView.setTitleFormatter { day ->
            val year = day.year
            val month = String.format("%02d", day.month + 1)
            val headerText = "$year.$month"

            binding.tvCalendarHeader.text = headerText
            headerText
        }
    }

    private fun setupWeekdayFormatter(calendarView: MaterialCalendarView) {
        calendarView.setWeekDayFormatter(WeekDayColorFormatter(requireContext()))
    }

    private fun configureDateRange(calendarView: MaterialCalendarView) {
        val startDate = LocalDate.parse(viewModel.startDateState.value)
        val endDate = LocalDate.parse(viewModel.endDateState.value)
        val lastDayOfMonth = endDate.withDayOfMonth(endDate.lengthOfMonth()).dayOfMonth

        calendarView.state().edit()
            .setMinimumDate(CalendarDay.from(startDate.year, startDate.monthValue - 1, 1))
            .setMaximumDate(CalendarDay.from(endDate.year, endDate.monthValue - 1, lastDayOfMonth))
            .commit()
    }

    private fun setupDateChangeListener(calendarView: MaterialCalendarView) {
        calendarView.setOnDateChangedListener { _, date, _ ->
            binding.tvSelectedDate.text =
                String.format("%04d/%02d/%02d", date.year, date.month + 1, date.day)

            val selectedDate = LocalDate.of(date.year, date.month + 1, date.day)

            viewModel.calculateTimeSlots(
                selectedDate,
                viewModel.startTimeState.value!!,
                viewModel.endTimeState.value!!,
            )
            timeListAdapter.submitList(viewModel.timeSlots.value.toList())
        }
    }

    private fun navigateToVotePromisePlaceFragment(meetId: Int) {
        VotePromiseTimeFragmentDirections.actionVotePromiseTimeFragmentToVotePromisePlaceFragment(
            meetId
        )
            .apply {
                findNavController().navigate(this)
            }
    }
}