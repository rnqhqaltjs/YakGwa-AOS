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
import com.google.android.material.snackbar.Snackbar
import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentCreatePromiseBinding
import com.prography.yakgwa.model.SelectedLocationModel
import com.prography.yakgwa.model.ThemeModel
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CreatePromiseFragment :
    BaseFragment<FragmentCreatePromiseBinding>(R.layout.fragment_create_promise) {

    private val viewModel: CreatePromiseViewModel by viewModels()

    private lateinit var locationListAdapter: LocationListAdapter
    private lateinit var selectedLocationListAdapter: SelectedLocationListAdapter
    private lateinit var themeListAdapter: ThemeListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
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
            viewModel.selectedThemeState.collectLatest { selectedTheme ->
                themeListAdapter.submitList(selectedTheme)
            }
        }

        lifecycleScope.launch {
            viewModel.selectedLocationsState.collectLatest { selectedLocations ->
                selectedLocationListAdapter.submitList(selectedLocations)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.themesState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            themeListAdapter.submitList(
                                it.data.map { themeItem ->
                                    ThemeModel(themeItem)
                                }
                            )
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
                            navigateToInvitationLeaderFragment(it.data)
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
                viewModel.locationState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            locationListAdapter.submitList(it.data)
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        locationListAdapter = LocationListAdapter().apply {
            setOnItemClickListener { selectedLocation ->
                if (selectedLocationListAdapter.itemCount > MAX_SELECTED_COUNT) {
                    Snackbar.make(requireView(), "장소를 더 추가할 수 없어요.", Snackbar.LENGTH_SHORT).show()
                    binding.etSearchLocation.setText("")
                } else {
                    viewModel.addLocation(SelectedLocationModel(selectedLocation.title))
                    binding.etSearchLocation.setText("")
                }
            }
        }
        binding.rvSearchLocation.adapter = locationListAdapter

        selectedLocationListAdapter = SelectedLocationListAdapter().apply {
            setOnRemoveClickListener { selectedLocation ->
                viewModel.removeLocation(selectedLocation)
            }
        }
        binding.rvSelectedLocation.adapter = selectedLocationListAdapter

        themeListAdapter = ThemeListAdapter().apply {
            setOnItemClickListener { position ->
                viewModel.singleThemeSelection(position)
            }
        }
        binding.rvTema.adapter = themeListAdapter
    }

    private fun addListeners() {
        binding.etWithin20Msg.addTextChangedListener { text ->
            viewModel.onTextChanged20(text.toString())
        }

        binding.etWithin80Msg.addTextChangedListener { text ->
            viewModel.onTextChanged80(text.toString())
        }

        binding.etSearchLocation.addTextChangedListener { text ->
            viewModel.setSearchQuery(text.toString())
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
                    binding.etWithin20Msg.text.toString(),
                    binding.etWithin80Msg.text.toString(),
                    viewModel.selectedThemeState.value?.indexOfFirst { it.isSelected }!!,
                    viewModel.selectedLocationsState.value?.map { it.title }!!,
                    CreateMeetRequestEntity.VoteDateRange(
                        viewModel.selectedStartDate.value!!,
                        viewModel.selectedEndDate.value!!
                    ),
                    CreateMeetRequestEntity.VoteTimeRange(
                        formatTimeTo24Hour(viewModel.selectedStartTime.value!!),
                        formatTimeTo24Hour(viewModel.selectedEndTime.value!!)
                    ),
                    12
                )
            )
        }

        binding.navigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
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
            viewModel::updateStartTime -> viewModel.selectedStartTime.value ?: DEFAULT_START_TIME
            else -> viewModel.selectedEndTime.value ?: DEFAULT_END_TIME
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
        return dateString?.let {
            LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } ?: LocalDate.now()
    }

    private fun parseTime(timeString: String?): LocalTime {
        return timeString?.let {
            LocalTime.parse(it, DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN))
        } ?: LocalTime.now()
    }

    private fun formatTimeTo24Hour(timeString: String?): String {
        return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN))
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    private fun navigateToInvitationLeaderFragment(createMeetResponseEntity: CreateMeetResponseEntity) {
        CreatePromiseFragmentDirections.actionCreatePromiseFragmentToInvitationLeaderFragment(
            createMeetResponseEntity
        )
            .apply {
                findNavController().navigate(this)
            }
    }

    companion object {
        private const val MAX_SELECTED_COUNT = 2
        private const val DEFAULT_START_TIME = "오전 09:00"
        private const val DEFAULT_END_TIME = "오후 06:00"
    }
}