package com.prography.yakgwa.ui.vote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.domain.model.request.VotePlaceRequestEntity
import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.model.response.TimePlaceResponseEntity
import com.prography.domain.model.response.VoteInfoResponseEntity
import com.prography.domain.usecase.GetUserVoteInfoUsecase
import com.prography.domain.usecase.GetVoteTimePlaceCandidateInfoUseCase
import com.prography.domain.usecase.PostUserVotePlaceUseCase
import com.prography.domain.usecase.PostUserVoteTimeUseCase
import com.prography.yakgwa.model.PlaceModel
import com.prography.yakgwa.model.TimeModel
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.dateTimeUtils.DateTimeUtils.formatLocalDateTimeToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val getVoteTimePlaceCandidateInfoUseCase: GetVoteTimePlaceCandidateInfoUseCase,
    private val postUserVoteTimeUseCase: PostUserVoteTimeUseCase,
    private val postUserVotePlaceUseCase: PostUserVotePlaceUseCase,
    private val getUserVoteInfoUseCase: GetUserVoteInfoUsecase,
    private val localStorage: YakGwaLocalDataSource
) : ViewModel() {

    private val _timePlaceState =
        MutableStateFlow<UiState<TimePlaceResponseEntity>>(UiState.Loading)
    val timePlaceState = _timePlaceState.asStateFlow()

    private val _timeSlotsState = MutableStateFlow<Map<LocalDate, List<TimeModel>>>(emptyMap())
    val timeSlotsState = _timeSlotsState

    private val _selectedDateState = MutableStateFlow<LocalDate?>(null)
    val selectedDateState = _selectedDateState

    private val _selectedTimeState = MutableStateFlow<List<TimeModel>>(emptyList())
    val selectedTimeState = _selectedTimeState

    private val _startDate = MutableStateFlow<LocalDate?>(null)
    val startDate = _startDate

    private val _endDate = MutableStateFlow<LocalDate?>(null)
    val endDate = _endDate

    private val _selectedPlaceState = MutableStateFlow<List<PlaceModel>?>(null)
    val selectedPlaceState = _selectedPlaceState

    private val _timeVoteState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val timeVoteState = _timeVoteState.asStateFlow()

    private val _placeVoteState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val placeVoteState = _placeVoteState.asStateFlow()

    private val _voteInfoState = MutableStateFlow<UiState<VoteInfoResponseEntity>>(UiState.Loading)
    val voteInfoState = _voteInfoState.asStateFlow()

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
        val timeSlotsForSelectedDate = _timeSlotsState.value[date].orEmpty()

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
        val currentList = _selectedPlaceState.value.orEmpty().mapIndexed { index, item ->
            item.copy(isSelected = index == position)
        }
        _selectedPlaceState.value = currentList
    }

    fun voteTime(meetId: Int) {
        _timeVoteState.value = UiState.Loading

        viewModelScope.launch {
            val userId = userId()
            val voteTimeRequestEntity = convertTimeModelsToVoteTimeRequest(_timeSlotsState.value)

            postUserVoteTimeUseCase(userId, meetId, voteTimeRequestEntity)
                .onSuccess {
                    _timeVoteState.value = UiState.Success(it)
                }
                .onFailure {
                    _timeVoteState.value = UiState.Failure(it.message)
                }
        }
    }

    fun votePlace(meetId: Int) {
        _placeVoteState.value = UiState.Loading

        viewModelScope.launch {
            val userId = userId()
            val selectedPlaceIds =
                VotePlaceRequestEntity(
                    _selectedPlaceState.value
                        .orEmpty()
                        .filter { it.isSelected }
                        .map { it.placeItem.candidatePlaceId }
                )

            postUserVotePlaceUseCase(userId, meetId, selectedPlaceIds)
                .onSuccess {
                    _placeVoteState.value = UiState.Success(it)
                }
                .onFailure {
                    _placeVoteState.value = UiState.Failure(it.message)
                }
        }
    }

    fun getVoteInfo(meetId: Int) {
        _voteInfoState.value = UiState.Loading

        viewModelScope.launch {
            val userId = userId()

            getUserVoteInfoUseCase(userId, meetId)
                .onSuccess {
                    _voteInfoState.value = UiState.Success(it)
                }
                .onFailure {
                    _voteInfoState.value = UiState.Failure(it.message)
                }
        }
    }

    private fun convertTimeModelsToVoteTimeRequest(timeModels: Map<LocalDate, List<TimeModel>>): VoteTimeRequestEntity {
        val possibleSchedules = mutableListOf<VoteTimeRequestEntity.PossibleSchedule>()

        timeModels.forEach { (date, timeModelList) ->
            timeModelList.filter { it.isSelected }
                .forEach { selectedTimeModel ->
                    val startTime = selectedTimeModel.time
                    val endTime = startTime.plusHours(1)

                    possibleSchedules.add(
                        VoteTimeRequestEntity.PossibleSchedule(
                            possibleStartTime = formatLocalDateTimeToString(date, startTime),
                            possibleEndTime = formatLocalDateTimeToString(date, endTime)
                        )
                    )
                }
        }
        return VoteTimeRequestEntity(possibleSchedules)
    }

    private suspend fun userId(): Int {
        return withContext(Dispatchers.IO) {
            localStorage.userId.first()
        }
    }
}