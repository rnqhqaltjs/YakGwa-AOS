package com.prography.yakgwa.ui.createPromise.createPromiseTime

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentDirectInputTimeBinding
import com.prography.yakgwa.ui.createPromise.CreatePromiseViewModel
import com.prography.yakgwa.util.base.BaseFragment
import com.prography.yakgwa.util.dateTimeUtils.DateTimeUtils.parseDateFromString
import com.prography.yakgwa.util.dateTimeUtils.DateTimeUtils.parseTimeFromString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DirectInputTimeFragment :
    BaseFragment<FragmentDirectInputTimeBinding>(R.layout.fragment_direct_input_time) {
    private val viewModel: CreatePromiseViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        addListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun observer() {
        lifecycleScope.launch {
            viewModel.selectedStartDate.collectLatest { date ->
                binding.tvDate.text = date
            }
        }

        lifecycleScope.launch {
            viewModel.selectedStartTime.collectLatest { time ->
                binding.tvTime.text = time
            }
        }
    }

    private fun addListeners() {
        binding.cvDate.setOnClickListener {
            showDatePickerDialog(viewModel::updateStartDate)
        }

        binding.cvTime.setOnClickListener {
            showTimePickerDialog(viewModel::updateStartTime)
        }
    }

    private fun showDatePickerDialog(updateDate: (Int, Int, Int) -> Unit) {
        val dateFromDialog =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                updateDate(year, monthOfYear + 1, dayOfMonth)
            }

        val selectedDate = when (updateDate) {
            viewModel::updateStartDate -> viewModel.selectedStartDate.value
            else -> viewModel.selectedEndDate.value ?: viewModel.selectedStartDate.value
        }

        val date = parseDateFromString(selectedDate)
        DatePickerDialog(
            requireContext(), dateFromDialog,
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        ).show()
    }

    private fun showTimePickerDialog(updateTime: (Int, Int) -> Unit) {
        val timeFromDialog =
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                updateTime(hourOfDay, minute)
            }

        val selectedTime = when (updateTime) {
            viewModel::updateStartTime -> viewModel.selectedStartTime.value ?: DEFAULT_START_TIME
            else -> viewModel.selectedEndTime.value ?: DEFAULT_END_TIME
        }

        val time = parseTimeFromString(selectedTime)
        TimePickerDialog(
            requireContext(), timeFromDialog,
            time.hour,
            time.minute,
            false
        ).show()
    }

    companion object {
        const val DEFAULT_START_TIME = "오전 09:00"
        const val DEFAULT_END_TIME = "오후 06:00"
    }
}