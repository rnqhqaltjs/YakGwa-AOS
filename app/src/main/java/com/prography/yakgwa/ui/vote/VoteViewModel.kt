package com.prography.yakgwa.ui.vote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.domain.model.request.PlaceCandidateRequestEntity
import com.prography.domain.model.request.VotePlaceRequestEntity
import com.prography.domain.model.request.VoteTimeRequestEntity
import com.prography.domain.model.response.LocationResponseEntity
import com.prography.domain.model.response.PlaceCandidateResponseEntity
import com.prography.domain.model.response.TimeCandidateResponseEntity
import com.prography.domain.usecase.GetLocationListUseCase
import com.prography.domain.usecase.GetPlaceCandidateInfoUseCase
import com.prography.domain.usecase.GetVoteTimeCandidateInfoUseCase
import com.prography.domain.usecase.PostPlaceCandidateInfoUseCase
import com.prography.domain.usecase.PostUserVotePlaceUseCase
import com.prography.domain.usecase.PostUserVoteTimeUseCase
import com.prography.yakgwa.model.PlaceModel
import com.prography.yakgwa.model.SelectedLocationModel
import com.prography.yakgwa.model.TimeModel
import com.prography.yakgwa.util.DateTimeUtils.formatLocalDateTimeToString
import com.prography.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPlaceCandidateInfoUseCase: GetPlaceCandidateInfoUseCase,
    private val getVoteTimeCandidateInfoUseCase: GetVoteTimeCandidateInfoUseCase,
    private val postUserVoteTimeUseCase: PostUserVoteTimeUseCase,
    private val postUserVotePlaceUseCase: PostUserVotePlaceUseCase,
    private val getLocationListUseCase: GetLocationListUseCase,
    private val postPlaceCandidateInfoUseCase: PostPlaceCandidateInfoUseCase
) : ViewModel() {
    var meetId: Int
        get() = savedStateHandle.get<Int>(MEET_ID) ?: INVALID_MEET_ID
        set(value) {
            savedStateHandle[MEET_ID] = value
        }

    private val _placeCandidateState =
        MutableStateFlow<UiState<List<PlaceCandidateResponseEntity>>>(UiState.Loading)
    val placeCandidateState = _placeCandidateState
        .onSubscription {
            getVotePlaceCandidate()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    private val _timeCandidateState =
        MutableStateFlow<UiState<TimeCandidateResponseEntity>>(UiState.Loading)
    val timeCandidateState = _timeCandidateState
        .onSubscription {
            getVoteTimeCandidate()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

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

    private val _selectedPlaceState = MutableStateFlow<List<PlaceModel>>(emptyList())
    val selectedPlaceState = _selectedPlaceState

    private val _timeVoteState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val timeVoteState = _timeVoteState.asStateFlow()

    private val _placeVoteState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val placeVoteState = _placeVoteState.asStateFlow()

    private val _candidateLocationState =
        MutableStateFlow<UiState<List<LocationResponseEntity>>>(UiState.Loading)
    val candidateLocationState = _candidateLocationState.asStateFlow()

    private val _selectedCandidateLocationState =
        MutableStateFlow<List<SelectedLocationModel>>(emptyList())
    val selectedCandidateLocationState = _selectedCandidateLocationState

    private val _addPlaceState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val addPlaceState = _addPlaceState.asStateFlow()

    fun getVotePlaceCandidate() {
        _placeCandidateState.value = UiState.Loading

        viewModelScope.launch {
            getPlaceCandidateInfoUseCase(meetId)
                .onSuccess {
                    _selectedPlaceState.value = it.map { placeEntity ->
                        PlaceModel(placeEntity)
                    }
                    _placeCandidateState.value = UiState.Success(it)
                }
                .onFailure {
                    _placeCandidateState.value = UiState.Failure(it.message)
                }
        }
    }

    private fun getVoteTimeCandidate() {
        _timeCandidateState.value = UiState.Loading

        viewModelScope.launch {
            getVoteTimeCandidateInfoUseCase(meetId)
                .onSuccess {
                    _timeCandidateState.value = UiState.Success(it)
                }
                .onFailure {
                    _timeCandidateState.value = UiState.Failure(it.message)
                }
        }
    }

    fun calculateTimeSlots(
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        val timeSlotsPerDate = mutableMapOf<LocalDate, List<TimeModel>>()

        var currentDate = startDate
        while (!currentDate.isAfter(endDate)) {
            val timeSlots = generateSequence(LocalTime.MIN) { it.plusHours(1) }
                .takeWhile { !it.isAfter(LocalTime.MAX.minusHours(1)) }
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
        val currentList = _selectedPlaceState.value.mapIndexed { index, item ->
            if (index == position) {
                item.copy(isSelected = !item.isSelected)
            } else {
                item
            }
        }
        _selectedPlaceState.value = currentList
    }

    fun voteTime() {
        _timeVoteState.value = UiState.Loading

        viewModelScope.launch {
            val voteTimeRequestEntity = convertTimeModelsToVoteTimeRequest(_timeSlotsState.value)

            postUserVoteTimeUseCase(meetId, voteTimeRequestEntity)
                .onSuccess {
                    _timeVoteState.value = UiState.Success(it)
                }
                .onFailure {
                    _timeVoteState.value = UiState.Failure(it.message)
                }
        }
    }

    fun votePlace() {
        _placeVoteState.value = UiState.Loading

        viewModelScope.launch {
            val selectedPlaceIds =
                VotePlaceRequestEntity(
                    _selectedPlaceState.value
                        .filter { it.isSelected }
                        .map { it.placeItem.placeSlotId }
                )

            postUserVotePlaceUseCase(meetId, selectedPlaceIds)
                .onSuccess {
                    _placeVoteState.value = UiState.Success(it)
                }
                .onFailure {
                    _placeVoteState.value = UiState.Failure(it.message)
                }
        }
    }

    private fun convertTimeModelsToVoteTimeRequest(timeModels: Map<LocalDate, List<TimeModel>>): VoteTimeRequestEntity {
        val possibleSchedules = mutableListOf<VoteTimeRequestEntity.EnableTimes>()

        timeModels.forEach { (date, timeModelList) ->
            timeModelList.filter { it.isSelected }
                .forEach { selectedTimeModel ->
                    val enableTime = selectedTimeModel.time

                    possibleSchedules.add(
                        VoteTimeRequestEntity.EnableTimes(
                            enableTime = formatLocalDateTimeToString(date, enableTime),
                        )
                    )
                }
        }
        return VoteTimeRequestEntity(possibleSchedules)
    }

    fun getCandidateLocations(query: String) {
        _candidateLocationState.value = UiState.Loading

        viewModelScope.launch {
            runCatching {
                getLocationListUseCase(query).collect {
                    _selectedCandidateLocationState.value = it.map { locationEntity ->
                        SelectedLocationModel(locationEntity)
                    }
                    _candidateLocationState.value = UiState.Success(it)
                }
            }.onFailure {
                _candidateLocationState.value = UiState.Failure(it.message)
            }
        }
    }

    fun singleCandidateLocationSelection(position: Int) {
        val currentList = _selectedCandidateLocationState.value.mapIndexed { index, item ->
            item.copy(isSelected = index == position)
        }
        _selectedCandidateLocationState.value = currentList
    }

    fun addPlaceCandidate() {
        _addPlaceState.value = UiState.Loading

        val selectedLocation =
            _selectedCandidateLocationState.value.find { it.isSelected }?.locationResponseEntity
        selectedLocation?.let { location ->
            val requestEntity = PlaceCandidateRequestEntity(
                location.title,
                location.link,
                location.category,
                location.description,
                location.telephone,
                location.address,
                location.roadAddress,
                location.mapX,
                location.mapY
            )

            viewModelScope.launch {
                postPlaceCandidateInfoUseCase(meetId, requestEntity)
                    .onSuccess {
                        _addPlaceState.value = UiState.Success(it)
                    }
                    .onFailure {
                        _addPlaceState.value = UiState.Failure(it.message)
                    }
            }
        }
    }

    companion object {
        const val MEET_ID = "meetId"
        const val INVALID_MEET_ID = -1
    }
}