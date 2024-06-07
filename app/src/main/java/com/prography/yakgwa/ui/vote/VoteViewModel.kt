package com.prography.yakgwa.ui.vote

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.domain.model.response.TimePlaceResponseEntity
import com.prography.domain.usecase.GetVoteTimePlaceCandidateInfoUseCase
import com.prography.yakgwa.model.DateTimeModel
import com.prography.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class VoteViewModel @Inject constructor(
    private val getVoteTimePlaceCandidateInfoUseCase: GetVoteTimePlaceCandidateInfoUseCase
) : ViewModel() {

    private val _timePlaceState =
        MutableStateFlow<UiState<TimePlaceResponseEntity>>(UiState.Loading)
    val timePlaceState = _timePlaceState.asStateFlow()

    private val _timeSlots = MutableStateFlow<List<DateTimeModel>>(emptyList())
    val timeSlots = _timeSlots.asStateFlow()

    private val _startDateState = MutableStateFlow<String?>(null)
    val startDateState = _startDateState.asStateFlow()

    private val _endDateState = MutableStateFlow<String?>(null)
    val endDateState = _endDateState.asStateFlow()

    private val _startTimeState = MutableStateFlow<String?>(null)
    val startTimeState = _startTimeState.asStateFlow()

    private val _endTimeState = MutableStateFlow<String?>(null)
    val endTimeState = _endTimeState.asStateFlow()

    private val _selectedTimeState = MutableStateFlow<List<DateTimeModel>?>(null)
    val selectedTimeState = _selectedTimeState.asStateFlow()

    fun getTimePlaceCandidate(meetId: Int) {
        _timePlaceState.value = UiState.Loading

        viewModelScope.launch {
            getVoteTimePlaceCandidateInfoUseCase(meetId)
                .onSuccess {
                    _startDateState.value = it.timeItems.dateRange.start
                    _endDateState.value = it.timeItems.dateRange.end
                    _startTimeState.value = it.timeItems.timeRange.start
                    _endTimeState.value = it.timeItems.timeRange.end
                    _timePlaceState.value = UiState.Success(it)
                }
                .onFailure {
                    _timePlaceState.value = UiState.Failure(it.message)
                }
        }
    }

    fun calculateTimeSlots(date: LocalDate, startTime: String, endTime: String) {
        val start = LocalTime.parse(startTime)
        val end = LocalTime.parse(endTime)

        val timeSlots = generateSequence(start) { it.plusHours(1) }
            .takeWhile { !it.isAfter(end) }
            .map { DateTimeModel(date, it) }
            .toList()

        _timeSlots.value = timeSlots
    }

//    fun selectTimeSLot() {
//        val items = _selectedTimeState.value.orEmpty().toMutableList()
//
//        val selectedItem = items
//        val updatedItem = selectedItem.copy(
//            isSelected = !selectedItem.isSelected,
//            voteCount = if (!selectedItem.isSelected) selectedItem.voteCount + 1 else selectedItem.voteCount - 1
//        )
//
//        items[position] = updatedItem
//
//    }
}