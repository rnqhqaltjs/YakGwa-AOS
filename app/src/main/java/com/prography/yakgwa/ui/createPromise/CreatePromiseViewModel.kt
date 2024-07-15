package com.prography.yakgwa.ui.createPromise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.domain.model.response.LocationResponseEntity
import com.prography.domain.model.response.ThemesResponseEntity
import com.prography.domain.usecase.GetLocationListUseCase
import com.prography.domain.usecase.GetThemeListUseCase
import com.prography.domain.usecase.PostNewMeetCreateUseCase
import com.prography.yakgwa.model.SelectedLocationModel
import com.prography.yakgwa.model.ThemeModel
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.dateTimeUtils.DateTimeUtils.formatDateToString
import com.prography.yakgwa.util.dateTimeUtils.DateTimeUtils.formatTimeTo24Hour
import com.prography.yakgwa.util.dateTimeUtils.DateTimeUtils.formatTimeToString
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CreatePromiseViewModel @Inject constructor(
    private val getThemeListUseCase: GetThemeListUseCase,
    private val postNewMeetCreateUseCase: PostNewMeetCreateUseCase,
    private val getLocationListUseCase: GetLocationListUseCase
) : ViewModel() {

    private val _textLength20State = MutableStateFlow("")
    val textLength20State = _textLength20State

    private val _textLength80State = MutableStateFlow("")
    val textLength80State = _textLength80State

    private val _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate = _selectedDate

    private val _selectedTime = MutableStateFlow<String?>(null)
    val selectedTime = _selectedTime

    private val _searchQueryState = MutableStateFlow("")

    private val _themesState =
        MutableStateFlow<UiState<List<ThemesResponseEntity>>>(UiState.Loading)
    val themesState = _themesState
        .onSubscription {
            getThemes()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    private val _createMeetState =
        MutableStateFlow<UiState<CreateMeetResponseEntity>>(UiState.Loading)
    val createMeetState = _createMeetState.asStateFlow()


    private val _directLocationState =
        MutableSharedFlow<UiState<List<LocationResponseEntity>>>()
    val directLocationState = _directLocationState.onSubscription {
        getDirectLocations()
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    private val _candidateLocationState =
        MutableStateFlow<UiState<List<LocationResponseEntity>>>(UiState.Loading)
    val candidateLocationState = _candidateLocationState.asStateFlow()

    private val _selectedThemeState = MutableStateFlow<List<ThemeModel>>(emptyList())
    val selectedThemeState = _selectedThemeState

    private val _selectedDirectLocationState =
        MutableStateFlow<List<LocationResponseEntity>>(emptyList())
    val selectedDirectLocationState = _selectedDirectLocationState

    private val _selectedCandidateLocationState =
        MutableStateFlow<List<LocationResponseEntity>>(emptyList())
    val selectedCandidateLocationState = _selectedCandidateLocationState

    private val _selectedCandidateLocationDetailState =
        MutableStateFlow<List<SelectedLocationModel>>(emptyList())
    val selectedCandidateLocationDetailState = _selectedCandidateLocationDetailState

    private val _selectedTabTimeIndex = MutableStateFlow(TAB_ADD_CANDIDATE)
    val selectedTabTimeIndex = _selectedTabTimeIndex

    private val _selectedCalendarDates = MutableStateFlow<List<CalendarDay>>(emptyList())
    val selectedCalendarDates = _selectedCalendarDates

    private val _selectedTabPlaceIndex = MutableStateFlow(TAB_ADD_CANDIDATE)
    val selectedTabPlaceIndex = _selectedTabPlaceIndex

    private val _isAddCandidateBtnClicked = MutableStateFlow(false)
    val isAddCandidateBtnClicked = _isAddCandidateBtnClicked

    private fun getThemes() {
        _themesState.value = UiState.Loading

        viewModelScope.launch {
            getThemeListUseCase()
                .onSuccess { themeList ->
                    _selectedThemeState.value = themeList.map { themeEntity ->
                        ThemeModel(themeEntity)
                    }
                    _themesState.value = UiState.Success(themeList)
                }
                .onFailure {
                    _themesState.value = UiState.Failure(it.message)
                }
        }
    }

    fun onTextChanged20(text: String) {
        _textLength20State.value = text
    }

    fun onTextChanged80(text: String) {
        _textLength80State.value = text
    }

    fun updateStartDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _selectedDate.value = formatDateToString(year, monthOfYear, dayOfMonth)
    }

    fun updateStartTime(hourOfDay: Int, minute: Int) {
        _selectedTime.value = formatTimeToString(hourOfDay, minute)
    }

    fun createMeet() {
        _createMeetState.value = UiState.Loading

        viewModelScope.launch {
            val createMeetRequestEntity = buildCreateMeetRequestEntity()

            postNewMeetCreateUseCase(createMeetRequestEntity)
                .onSuccess {
                    _createMeetState.value = UiState.Success(it)
                }
                .onFailure {
                    _createMeetState.value = UiState.Failure(it.message)
                }
        }
    }

    private fun buildCreateMeetRequestEntity(): CreateMeetRequestEntity {
        val placeInfoList = when (_selectedTabPlaceIndex.value) {
            TAB_ADD_CANDIDATE -> _selectedCandidateLocationState.value
            TAB_DIRECT_INPUT -> _selectedDirectLocationState.value
            else -> emptyList()
        }

        val voteDate = _selectedCalendarDates.value.run {
            if (_selectedTabTimeIndex.value == TAB_ADD_CANDIDATE) {
                CreateMeetRequestEntity.VoteDate(
                    startVoteDate = LocalDate.of(first().year, first().month + 1, first().day)
                        .toString(),
                    endVoteDate = LocalDate.of(last().year, last().month + 1, last().day).toString()
                )
            } else {
                null
            }
        }

        val meetTime = if (_selectedTabTimeIndex.value == TAB_DIRECT_INPUT) {
            "${_selectedDate.value} ${formatTimeTo24Hour(_selectedTime.value!!)}"
        } else {
            null
        }

        return CreateMeetRequestEntity(
            meetTitle = _textLength20State.value,
            description = _textLength80State.value,
            meetThemeId = _selectedThemeState.value.find { it.isSelected }?.themesResponseEntity?.themeId!!,
            confirmPlace = _selectedTabPlaceIndex.value == TAB_DIRECT_INPUT,
            placeInfo = placeInfoList.map { place ->
                CreateMeetRequestEntity.PlaceInfo(
                    title = place.title,
                    link = place.link,
                    category = place.category,
                    description = place.description,
                    telephone = place.telephone,
                    address = place.address,
                    roadAddress = place.roadAddress,
                    mapx = place.mapX,
                    mapy = place.mapY
                )
            },
            voteDate = voteDate,
            meetTime = meetTime
        )
    }

    fun setSearchQuery(query: String) {
        _searchQueryState.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun getDirectLocations() {
        viewModelScope.launch {
            _searchQueryState
                .debounce(SEARCH_QUERY_DEBOUNCE_DELAY)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isNotBlank()) {
                        _directLocationState.emit(UiState.Loading)

                        getLocationListUseCase(query)
                            .catch { e ->
                                _directLocationState.emit(UiState.Failure(e.message))
                            }
                    } else {
                        flowOf(emptyList())
                    }
                }
                .collect { result ->
                    _directLocationState.emit(UiState.Success(result))
                }
        }
    }

    fun getCandidateLocations(query: String) {
        _candidateLocationState.value = UiState.Loading

        viewModelScope.launch {
            runCatching {
                getLocationListUseCase(query).collect {
                    _selectedCandidateLocationDetailState.value = it.map { locationEntity ->
                        SelectedLocationModel(locationEntity)
                    }
                    _candidateLocationState.value = UiState.Success(it)
                }
            }.onFailure {
                _candidateLocationState.value = UiState.Failure(it.message)
            }
        }
    }

    fun singleThemeSelection(position: Int) {
        val currentList = _selectedThemeState.value.mapIndexed { index, item ->
            item.copy(isSelected = index == position)
        }
        _selectedThemeState.value = currentList
    }

    fun singleCandidateLocationSelection(position: Int) {
        val currentList = _selectedCandidateLocationDetailState.value.mapIndexed { index, item ->
            item.copy(isSelected = index == position)
        }
        _selectedCandidateLocationDetailState.value = currentList
    }

    fun removeCandidateLocation(location: LocationResponseEntity) {
        val currentList = _selectedCandidateLocationState.value
        _selectedCandidateLocationState.value = currentList - location
    }

    fun addDirectLocation(location: LocationResponseEntity) {
        val currentList = _selectedDirectLocationState.value
        _selectedDirectLocationState.value = currentList + location
    }

    fun removeDirectLocation(location: LocationResponseEntity) {
        val currentList = _selectedDirectLocationState.value
        _selectedDirectLocationState.value = currentList - location
    }

    fun addCandidateLocation() {
        val currentList = _selectedCandidateLocationState.value
        val selectedLocation =
            _selectedCandidateLocationDetailState.value.find { it.isSelected }?.locationResponseEntity

        selectedLocation?.let {
            _selectedCandidateLocationState.value = currentList + it
        }
    }

    fun setSelectedDates(dates: List<CalendarDay>) {
        _selectedCalendarDates.value = dates
    }

    val isTimeBtnEnabled: StateFlow<Boolean> = combine(
        _selectedTabTimeIndex,
        _selectedCalendarDates,
        _selectedDate,
        _selectedTime
    ) { tabIndex, dates, startDate, startTime ->
        when (tabIndex) {
            TAB_ADD_CANDIDATE -> dates.isNotEmpty()
            TAB_DIRECT_INPUT -> startDate != null && startTime != null
            else -> false
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    val isPlaceBtnEnabled: StateFlow<Boolean> = combine(
        _selectedTabPlaceIndex,
        _selectedCandidateLocationState,
        _selectedDirectLocationState
    ) { tabIndex, candidateLocation, directLocation ->
        when (tabIndex) {
            TAB_ADD_CANDIDATE -> candidateLocation.isNotEmpty()
            TAB_DIRECT_INPUT -> directLocation.isNotEmpty()
            else -> false
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    fun onAddCandidateClicked() {
        _isAddCandidateBtnClicked.value = true
    }

    fun onResetButtonClicked() {
        _isAddCandidateBtnClicked.value = false
    }

    /*
    원래는 hiltNavGraphViewModels를 쓰려고 했으나 자식 프래그먼트가 부모랑 같은 nav를 공유 할 수 없어
    activityViewModels로 변경 및 홈으로 갔을때 임의로 데이터 초기화
     */
    fun clearData() {
        _textLength20State.value = ""
        _textLength80State.value = ""
        _selectedThemeState.value = _selectedThemeState.value.map {
            it.copy(isSelected = false)
        }
        _selectedTabTimeIndex.value = TAB_ADD_CANDIDATE
        _selectedCalendarDates.value = emptyList()
        _selectedDate.value = null
        _selectedTime.value = null
        _selectedTabPlaceIndex.value = TAB_ADD_CANDIDATE
        _candidateLocationState.value = UiState.Success(emptyList())
        _selectedCandidateLocationState.value = emptyList()
        _selectedCandidateLocationDetailState.value = emptyList()
        _selectedDirectLocationState.value = emptyList()
    }

    companion object {
        const val SEARCH_QUERY_DEBOUNCE_DELAY = 300L
        const val TAB_ADD_CANDIDATE = 0
        const val TAB_DIRECT_INPUT = 1
    }
}