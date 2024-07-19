package com.prography.yakgwa.ui.createPromise.createPromiseTime

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
import java.time.LocalDate

@AndroidEntryPoint
class DirectInputTimeFragment :
    BaseFragment<FragmentDirectInputTimeBinding>(R.layout.fragment_direct_input_time) {
    private val viewModel: CreatePromiseViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        addListeners()
    }

    private fun observer() {
        lifecycleScope.launch {
            viewModel.selectedDirectDate.collectLatest { date ->
                binding.tvDate.text = date
            }
        }

        lifecycleScope.launch {
            viewModel.selectedDirectTime.collectLatest { time ->
                binding.tvTime.text = time
            }
        }
    }

    private fun addListeners() {
        binding.cvDate.setOnClickListener {
            showDatePickerDialog(viewModel::updateDirectDate)
        }

        binding.cvTime.setOnClickListener {
            showTimePickerDialog(viewModel::updateDirectTime)
        }
    }

    private fun showDatePickerDialog(updateDate: (Int, Int, Int) -> Unit) {
        val dateFromDialog =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                updateDate(year, monthOfYear + 1, dayOfMonth)
            }

        val date = if (viewModel.selectedDirectDate.value.isEmpty()) {
            LocalDate.now()
        } else {
            parseDateFromString(viewModel.selectedDirectDate.value)
        }

        DatePickerDialog(
            requireContext(),
            dateFromDialog,
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        ).show()
    }

    private fun showTimePickerDialog(updateTime: (Int, Int) -> Unit) {
        val timeFromDialog = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            updateTime(hourOfDay, minute)
        }

        val time = if (viewModel.selectedDirectTime.value.isEmpty()) {
            parseTimeFromString(DEFAULT_START_TIME)
        } else {
            parseTimeFromString(viewModel.selectedDirectTime.value)
        }

        TimePickerDialog(
            requireContext(), timeFromDialog,
            time.hour,
            time.minute,
            false
        ).show()
    }

    companion object {
        const val DEFAULT_START_TIME = "오전 09:00"
    }
}