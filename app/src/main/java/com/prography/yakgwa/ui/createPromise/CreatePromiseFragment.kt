package com.prography.yakgwa.ui.createPromise

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentCreatePromiseBinding
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CreatePromiseFragment :
    BaseFragment<FragmentCreatePromiseBinding>(R.layout.fragment_create_promise) {

    private val viewModel: CreatePromiseViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()
        addListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun observer() {
        lifecycleScope.launch {
            viewModel.textLength20State.collectLatest { length ->
                binding.tvWithin20.text = "$length/20"
            }
        }

        lifecycleScope.launch {
            viewModel.textLength80State.collectLatest { length ->
                binding.tvWithin80.text = "$length/80"
            }
        }

        lifecycleScope.launch {
            viewModel.selectedStartDate.collectLatest { startDate ->
                binding.tvStartDate.text = startDate
            }
        }

        lifecycleScope.launch {
            viewModel.selectedEndDate.collectLatest { endDate ->
                binding.tvEndDate.text = endDate
            }
        }

        lifecycleScope.launch {
            viewModel.selectedStartTime.collectLatest { startTime ->
                binding.tvStartTime.text = startTime
            }
        }

        lifecycleScope.launch {
            viewModel.selectedEndTime.collectLatest { endTime ->
                binding.tvEndTime.text = endTime
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.themesState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.createMeetState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            CreatePromiseFragmentDirections.actionCreatePromiseFragmentToInvitationLeaderFragment()
                                .apply {
                                    findNavController().navigate(this)
                                }
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }

    private fun addListeners() {
        binding.etWithin20Msg.addTextChangedListener { text ->
            viewModel.onTextChanged20(text.toString())
        }

        binding.etWithin80Msg.addTextChangedListener { text ->
            viewModel.onTextChanged80(text.toString())
        }

        binding.startDate.setOnClickListener {
            selectStartDate()
        }

        binding.endDate.setOnClickListener {
            selectEndDate()
        }

        binding.startTime.setOnClickListener {
            selectStartTime()
        }

        binding.endTime.setOnClickListener {
            selectEndTime()
        }

        binding.btnCreatePromise.setOnClickListener {
            viewModel.createMeet(
                CreateMeetRequestEntity(
                    binding.tvPromiseInformation.text.toString(),
                    binding.tvPromiseDescription.text.toString(),
                    0,
                    false,
                    listOf("테스트"),
                    false,
                    CreateMeetRequestEntity.VoteDateRange(
                        viewModel.selectedStartDate.value!!,
                        viewModel.selectedEndDate.value!!
                    ),
                    CreateMeetRequestEntity.VoteTimeRange(
                        CreateMeetRequestEntity.Start(9, 0, 0, 0),
                        CreateMeetRequestEntity.End(18, 0, 0, 0)
                    )
                )
            )
        }
    }

    private fun selectStartDate() {
        showDatePickerDialog(viewModel::updateStartDate)
    }

    private fun selectEndDate() {
        showDatePickerDialog(viewModel::updateEndDate)
    }

    private fun selectStartTime() {
        showTimePickerDialog(viewModel::updateStartTime)
    }

    private fun selectEndTime() {
        showTimePickerDialog(viewModel::updateEndTime)
    }

    private fun showDatePickerDialog(updateDate: (Int, Int, Int) -> Unit) {
        val dateFromDialog =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                updateDate(year, monthOfYear + 1, dayOfMonth)
            }

        val selectedDate = when (updateDate) {
            viewModel::updateStartDate -> viewModel.selectedStartDate.value
            else -> viewModel.selectedEndDate.value
        }

        val date = parseDate(selectedDate)
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
            viewModel::updateStartTime -> viewModel.selectedStartTime.value
            else -> viewModel.selectedEndTime.value
        }

        val time = parseTime(selectedTime)
        TimePickerDialog(
            requireContext(), timeFromDialog,
            time.hour,
            time.minute,
            false
        ).show()
    }


    private fun parseDate(dateString: String?): LocalDate {
        return if (dateString != null) {
            LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } else {
            LocalDate.now()
        }
    }

    private fun parseTime(timeString: String?): LocalTime {
        return if (timeString != null) {
            LocalTime.parse(timeString, DateTimeFormatter.ofPattern("a hh:mm"))
        } else {
            LocalTime.now()
        }
    }
}