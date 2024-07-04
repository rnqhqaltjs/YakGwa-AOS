package com.prography.yakgwa.ui.createPromise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.datasource.local.YakGwaLocalDataSource
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CreatePromiseViewModel @Inject constructor(
    private val getThemeListUseCase: GetThemeListUseCase,
    private val postNewMeetCreateUseCase: PostNewMeetCreateUseCase,
    private val localStorage: YakGwaLocalDataSource,
    private val getLocationListUseCase: GetLocationListUseCase
) : ViewModel() {

    private val _textLength20State = MutableStateFlow("")
    val textLength20State = _textLength20State

    private val _textLength80State = MutableStateFlow("")
    val textLength80State = _textLength80State

    private val _selectedStartDate = MutableStateFlow<String?>(null)
    val selectedStartDate = _selectedStartDate

    private val _selectedEndDate = MutableStateFlow<String?>(null)
    val selectedEndDate = _selectedEndDate

    private val _selectedStartTime = MutableStateFlow<String?>(null)
    val selectedStartTime = _selectedStartTime

    private val _selectedEndTime = MutableStateFlow<String?>(null)
    val selectedEndTime = _selectedEndTime

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


    private val _locationState =
        MutableSharedFlow<UiState<List<LocationResponseEntity>>>()
    val locationState = _locationState.onSubscription {
        getLocations()
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    private val _selectedThemeState = MutableStateFlow<List<ThemeModel>>(emptyList())
    val selectedThemeState = _selectedThemeState

    private val _selectedLocationsState = MutableStateFlow<List<SelectedLocationModel>>(emptyList())
    val selectedLocationsState = _selectedLocationsState

    private var _selectedTabTimeIndex = MutableStateFlow(TAB_ADD_CANDIDATE)
    val selectedTabTimeIndex = _selectedTabTimeIndex

    private val _selectedDates = MutableStateFlow<List<LocalDate>>(emptyList())
    val selectedDates = _selectedDates

    private fun getThemes() {
        _themesState.value = UiState.Loading

        viewModelScope.launch {
            getThemeListUseCase()
                .onSuccess {
                    _selectedThemeState.value = it.map { themeEntity ->
                        ThemeModel(themeEntity)
                    }
                    _themesState.value = UiState.Success(it)
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
        _selectedStartDate.value = formatDateToString(year, monthOfYear, dayOfMonth)
    }

    fun updateEndDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _selectedEndDate.value = formatDateToString(year, monthOfYear, dayOfMonth)
    }

    fun updateStartTime(hourOfDay: Int, minute: Int) {
        _selectedStartTime.value = formatTimeToString(hourOfDay, minute)
    }

    fun updateEndTime(hourOfDay: Int, minute: Int) {
        _selectedEndTime.value = formatTimeToString(hourOfDay, minute)
    }

    fun createMeet() {
        _createMeetState.value = UiState.Loading

        viewModelScope.launch {
            val userId = userId()
            val createMeetRequestEntity = CreateMeetRequestEntity(
                _textLength20State.value,
                _textLength80State.value,
                _selectedThemeState.value
                    .find { it.isSelected }
                    ?.themesResponseEntity
                    ?.meetThemeId!!,
                listOf("홍대역"),
                CreateMeetRequestEntity.VoteDateRange(
                    _selectedStartDate.value!!,
                    _selectedEndDate.value!!
                ),
                CreateMeetRequestEntity.VoteTimeRange(
                    formatTimeTo24Hour(_selectedStartTime.value!!),
                    formatTimeTo24Hour(_selectedEndTime.value!!)
                ),
                12)

            postNewMeetCreateUseCase(userId, createMeetRequestEntity)
                .onSuccess {
                    _createMeetState.value = UiState.Success(it)
                }
                .onFailure {
                    _createMeetState.value = UiState.Failure(it.message)
                }
        }
    }

    private suspend fun userId(): Int {
        return withContext(Dispatchers.IO) {
            localStorage.userId.first()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQueryState.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun getLocations() {
        viewModelScope.launch {
            _searchQueryState
                .debounce(SEARCH_QUERY_DEBOUNCE_DELAY)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isNotBlank()) {
                        _locationState.emit(UiState.Loading)

                        getLocationListUseCase(query)
                            .catch { e ->
                                _locationState.emit(UiState.Failure(e.message))
                            }
                    } else {
                        flowOf(emptyList())
                    }
                }
                .collect { result ->
                    _locationState.emit(UiState.Success(result))
                }
        }
    }

    fun singleThemeSelection(position: Int) {
        val currentList = _selectedThemeState.value.mapIndexed { index, item ->
            item.copy(isSelected = index == position)
        }
        _selectedThemeState.value = currentList
    }

    fun addLocation(location: SelectedLocationModel) {
        val currentList = _selectedLocationsState.value
        _selectedLocationsState.value = currentList + location
    }

    fun removeLocation(location: SelectedLocationModel) {
        val currentList = _selectedLocationsState.value
        _selectedLocationsState.value = currentList - location
    }

    fun setSelectedDates(dates: List<LocalDate>) {
        _selectedDates.value = dates
    }

    val isTimeBtnEnabled: StateFlow<Boolean> = combine(
        _selectedTabTimeIndex,
        _selectedDates,
        _selectedStartDate,
        _selectedStartTime
    ) { tabIndex, dates, startDate, startTime ->
        tabIndex == 0 && dates.isNotEmpty() || tabIndex == 1 && startDate != null && startTime != null
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    companion object {
        const val SEARCH_QUERY_DEBOUNCE_DELAY = 300L
        const val TAB_ADD_CANDIDATE = 0
        const val TAB_DIRECT_INPUT = 1
    }
}