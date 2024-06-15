package com.prography.yakgwa.ui.vote

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.domain.model.request.VotePlaceRequestEntity
import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.model.response.TimePlaceResponseEntity
import com.prography.domain.usecase.GetVoteTimePlaceCandidateInfoUseCase
import com.prography.domain.usecase.PostUserVotePlaceUseCase
import com.prography.domain.usecase.PostUserVoteTimeUseCase
import com.prography.yakgwa.model.PlaceModel
import com.prography.yakgwa.model.TimeModel
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
    private val getVoteTimePlaceCandidateInfoUseCase: GetVoteTimePlaceCandidateInfoUseCase,
    private val postUserVoteTimeUseCase: PostUserVoteTimeUseCase,
    private val postUserVotePlaceUseCase: PostUserVotePlaceUseCase
) : ViewModel() {

    private val _timePlaceState =
        MutableStateFlow<UiState<TimePlaceResponseEntity>>(UiState.Loading)
    val timePlaceState = _timePlaceState.asStateFlow()

    private val _timeSlotsState = MutableStateFlow<Map<LocalDate, List<TimeModel>>>(emptyMap())
    val timeSlotsState = _timeSlotsState.asStateFlow()

    private val _selectedDateState = MutableStateFlow<LocalDate?>(null)
    val selectedDateState = _selectedDateState.asStateFlow()

    private val _selectedTimeState = MutableStateFlow<List<TimeModel>>(emptyList())
    val selectedTimeState = _selectedTimeState.asStateFlow()

    private val _startDate = MutableStateFlow<LocalDate?>(null)
    val startDate = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<LocalDate?>(null)
    val endDate = _endDate.asStateFlow()

    private val _selectedPlaceState = MutableStateFlow<List<PlaceModel>?>(null)
    val selectedPlaceState = _selectedPlaceState.asStateFlow()

    private val _timeVoteState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val timeVoteState = _timeVoteState.asStateFlow()

    private val _placeVoteState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val placeVoteState = _timeVoteState.asStateFlow()

    fun getTimePlaceCandidate(meetId: Int) {
        _timePlaceState.value = UiState.Loading

        viewModelScope.launch {
            getVoteTimePlaceCandidateInfoUseCase(meetId)
                .onSuccess {
                    _selectedPlaceState.value = it.placeItems.map { placeEntity ->
                        PlaceModel(placeEntity)
                    }
                    _timePlaceState.value = UiState.Success(it)
                }
                .onFailure {
                    _timePlaceState.value = UiState.Failure(it.message)
                }
        }
    }

    fun calculateTimeSlots(
        startDate: LocalDate,
        endDate: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ) {
        val timeSlotsPerDate = mutableMapOf<LocalDate, List<TimeModel>>()

        var currentDate = startDate
        while (!currentDate.isAfter(endDate)) {
            val timeSlots = generateSequence(startTime) { it.plusHours(1) }
                .takeWhile { !it.isAfter(endTime) }
                .map { TimeModel(it) }
                .toList()

            timeSlotsPerDate[currentDate] = timeSlots
            currentDate = currentDate.plusDays(1)
        }

        _timeSlotsState.value = timeSlotsPerDate
    }

    fun selectedDate(date: LocalDate) {
        val timeSlotsForSelectedDate = _timeSlotsState.value[date] ?: emptyList()

        _selectedDateState.value = date
        _selectedTimeState.value = timeSlotsForSelectedDate
    }

    fun selectTimeSlot(date: LocalDate, position: Int) {
        val items = _timeSlotsState.value.toMutableMap()
        val selectedDateItems = items[date].orEmpty().toMutableList()

        if (position in selectedDateItems.indices) {
            val selectedItem = selectedDateItems[position]
            val updatedItem = selectedItem.copy(
                isSelected = !selectedItem.isSelected,
                voteCount = if (!selectedItem.isSelected) selectedItem.voteCount + 1 else selectedItem.voteCount - 1
            )
            selectedDateItems[position] = updatedItem
            items[date] = selectedDateItems

            _timeSlotsState.value = items
            _selectedTimeState.value = selectedDateItems
        }
    }

    fun setDateRange(start: LocalDate, end: LocalDate) {
        _startDate.value = start
        _endDate.value = end
    }

    fun singlePlaceSelection(position: Int) {
        val items = _selectedPlaceState.value.orEmpty().toMutableList()

        val selectedItem = items[position].copy(isSelected = true)
        items[position] = selectedItem

        for (i in items.indices) {
            if (i != position && items[i].isSelected) {
                val unselectedItem = items[i].copy(isSelected = false)
                items[i] = unselectedItem
            }
        }

        _selectedPlaceState.value = items
    }

    fun voteTime(meetId: Int, userId: Int, voteTimeRequestEntity: VoteTimeRequestEntity) {
        _timeVoteState.value = UiState.Loading

        viewModelScope.launch {
            postUserVoteTimeUseCase(meetId, userId, voteTimeRequestEntity)
                .onSuccess {
                    _timeVoteState.value = UiState.Success(it)
                }
                .onFailure {
                    _timeVoteState.value = UiState.Failure(it.message)
                }
        }
    }

    fun votePlace(meetId: Int, userId: Int, votePlaceRequestEntity: VotePlaceRequestEntity) {
        _placeVoteState.value = UiState.Loading

        viewModelScope.launch {
            postUserVotePlaceUseCase(meetId, userId, votePlaceRequestEntity)
                .onSuccess {
                    _placeVoteState.value = UiState.Success(it)
                }
                .onFailure {
                    _placeVoteState.value = UiState.Failure(it.message)
                }
        }
    }


}