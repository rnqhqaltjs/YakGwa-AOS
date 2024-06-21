package com.prography.yakgwa.ui.vote

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentVotePromiseTimeBinding
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import com.prography.yakgwa.util.calendarUtils.MinMaxDecorator
import com.prography.yakgwa.util.calendarUtils.SelectDayDecorator
import com.prography.yakgwa.util.calendarUtils.TimeSelectedDecorator
import com.prography.yakgwa.util.calendarUtils.WeekDayColorFormatter
import com.prography.yakgwa.util.dateTimeUtils.DateTimeUtils.toCalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DefaultLocale")
class VotePromiseTimeFragment :
    BaseFragment<FragmentVotePromiseTimeBinding>(R.layout.fragment_vote_promise_time) {

    private val viewModel: VoteViewModel by activityViewModels()

    private lateinit var timeListAdapter: TimeListAdapter
    private val args by navArgs<VotePromiseTimeFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val meetId = args.meetId

        setupRecyclerView()
        initView(meetId)
        observer()
        addListeners(meetId)
    }

    private fun initView(meetId: Int) {
        viewModel.getTimePlaceCandidate(meetId)
    }

    private fun initCalendarView(
        startDate: LocalDate,
        endDate: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ) {
        binding.calendarView.apply {
            setupTitleFormatter(this)
            setupWeekdayFormatter(this)
            configureCalendarView(this)
            configureDateRange(this, startDate, endDate)
            applyCalendarDecorators(this, startDate, endDate, null)
            setupDateChangeListener(this)
        }
        if (viewModel.timeSlotsState.value.isEmpty()) {
            viewModel.calculateTimeSlots(startDate, endDate, startTime, endTime)
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.timePlaceState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            val startDate = LocalDate.parse(it.data.timeItems.dateRange.start)
                            val endDate = LocalDate.parse(it.data.timeItems.dateRange.end)
                            val startTime = LocalTime.parse(it.data.timeItems.timeRange.start)
                            val endTime = LocalTime.parse(it.data.timeItems.timeRange.end)

                            viewModel.setDateRange(startDate, endDate)
                            initCalendarView(startDate, endDate, startTime, endTime)
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.selectedTimeState.collectLatest {
                timeListAdapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            viewModel.selectedDateState.collectLatest {
                binding.tvSelectedDate.text = it?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                applyCalendarDecorators(
                    binding.calendarView,
                    viewModel.startDate.value,
                    viewModel.endDate.value,
                    it
                )
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
            setOnItemClickListener { position ->
                viewModel.selectedDateState.value?.let {
                    viewModel.selectTimeSlot(it, position)
                }
            }
        }
        binding.rvTime.adapter = timeListAdapter
    }

    private fun configureCalendarView(calendarView: MaterialCalendarView) {
        calendarView.setTopbarVisible(false)
//        calendarView.isDynamicHeightEnabled = true
    }

    private fun applyCalendarDecorators(
        calendarView: MaterialCalendarView,
        startDate: LocalDate?,
        endDate: LocalDate?,
        selectedDate: LocalDate?,
    ) {
        calendarView.apply {
            removeDecorators()
            invalidateDecorators()

            startDate?.let { start ->
                endDate?.let { end ->
                    addDecorators(
                        MinMaxDecorator(
                            start.toCalendarDay(),
                            end.toCalendarDay()
                        ),
                        SelectDayDecorator(
                            requireContext(),
                            start.toCalendarDay(),
                            end.toCalendarDay()
                        ),
                        TimeSelectedDecorator(
                            requireContext(),
                            viewModel.timeSlotsState.value,
                            selectedDate
                        )
                    )
                }
            }
        }
    }

    private fun setupTitleFormatter(calendarView: MaterialCalendarView) {
        calendarView.setTitleFormatter { day ->
            val year = day.year
            val month = String.format("%02d", day.month + MONTH_OFFSET)
            val headerText = "$year.$month"

            binding.tvCalendarHeader.text = headerText
            headerText
        }
    }

    private fun setupWeekdayFormatter(calendarView: MaterialCalendarView) {
        calendarView.setWeekDayFormatter(WeekDayColorFormatter(requireContext()))
    }

    private fun configureDateRange(
        calendarView: MaterialCalendarView,
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        val lastDayOfMonth = endDate.withDayOfMonth(endDate.lengthOfMonth()).dayOfMonth

        calendarView.state().edit()
            .setMinimumDate(startDate.toCalendarDay(FIRST_DAY_OF_MONTH))
            .setMaximumDate(endDate.toCalendarDay(lastDayOfMonth))
            .commit()
    }

    private fun setupDateChangeListener(calendarView: MaterialCalendarView) {
        calendarView.setOnDateChangedListener { _, date, _ ->
            viewModel.selectedDate(LocalDate.of(date.year, date.month + MONTH_OFFSET, date.day))
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

    companion object {
        const val MONTH_OFFSET = 1
        const val FIRST_DAY_OF_MONTH = 1
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        viewModel.timeSlotsState.value = emptyMap()
//    }
}