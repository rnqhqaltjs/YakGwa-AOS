package com.yomo.yakgwa.ui.vote

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.FragmentVotePromiseTimeBinding
import com.yomo.yakgwa.util.DateTimeUtils.toCalendarDay
import com.yomo.yakgwa.util.UiState
import com.yomo.yakgwa.util.base.BaseFragment
import com.yomo.yakgwa.util.calendarUtils.MinMaxDecorator
import com.yomo.yakgwa.util.calendarUtils.SelectDayDecorator
import com.yomo.yakgwa.util.calendarUtils.TimeSelectedDecorator
import com.yomo.yakgwa.util.calendarUtils.WeekDayColorFormatter
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
@SuppressLint("DefaultLocale")
class VotePromiseTimeFragment :
    BaseFragment<FragmentVotePromiseTimeBinding>(R.layout.fragment_vote_promise_time) {
    private val viewModel: VoteViewModel by viewModels()
    private lateinit var timeListAdapter: TimeListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun initCalendarView(
        startDate: LocalDate,
        endDate: LocalDate,
    ) {
        binding.calendarView.apply {
            setupTitleFormatter(this)
            setupWeekdayFormatter(this)
            configureCalendarView(this)
            configureDateRange(this, startDate, endDate)
            applyCalendarDecorators(this, startDate, endDate, null)
            setupDateChangeListener(this)
        }
        viewModel.calculateTimeSlots(startDate, endDate)
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.timeCandidateState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            val startDate = LocalDate.parse(it.data.voteDate?.startVoteDate)
                            val endDate = LocalDate.parse(it.data.voteDate?.endVoteDate)

                            viewModel.setDateRange(startDate, endDate)
                            initCalendarView(startDate, endDate)
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.timeVoteState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> navigateToInvitationLeaderFragment()
                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.selectedTimeState.collectLatest { selectedTime ->
                timeListAdapter.submitList(selectedTime)
                binding.btnVoteComplete.isEnabled = !selectedTime.none { it.isSelected }
            }
        }

        lifecycleScope.launch {
            viewModel.selectedDateState.collectLatest {
                binding.tvSelectedDate.text = it?.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                applyCalendarDecorators(
                    binding.calendarView,
                    viewModel.startDate.value,
                    viewModel.endDate.value,
                    it
                )
                binding.cvTimeSlot.visibility = View.VISIBLE
            }
        }
    }

    private fun addListeners() {
        binding.btnVoteComplete.setOnClickListener {
            viewModel.voteTime()
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

    private fun navigateToInvitationLeaderFragment() {
        VotePromiseTimeFragmentDirections.actionVotePromiseTimeFragmentToInvitationLeaderFragment(
            viewModel.meetId
        ).apply {
            findNavController().navigate(this)
        }
    }

    companion object {
        private const val MONTH_OFFSET = 1
        private const val FIRST_DAY_OF_MONTH = 1
    }
}