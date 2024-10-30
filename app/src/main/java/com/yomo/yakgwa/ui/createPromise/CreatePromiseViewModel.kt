package com.yomo.yakgwa.ui.createPromise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.skydoves.sandwich.getOrElse
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.serialization.onErrorDeserialize
import com.skydoves.sandwich.suspendOnSuccess
import com.yomo.data.utils.ErrorResponse
import com.yomo.domain.model.request.CreateMeetRequestEntity
import com.yomo.domain.model.response.CreateMeetResponseEntity
import com.yomo.domain.model.response.LocationResponseEntity
import com.yomo.domain.model.response.ThemesResponseEntity
import com.yomo.domain.usecase.GetLocationListUseCase
import com.yomo.domain.usecase.GetThemeListUseCase
import com.yomo.domain.usecase.PostNewMeetCreateUseCase
import com.yomo.yakgwa.util.DateTimeUtils.formatDateToString
import com.yomo.yakgwa.util.DateTimeUtils.formatTimeTo24Hour
import com.yomo.yakgwa.util.DateTimeUtils.formatTimeToString
import com.yomo.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _selectedDirectDate = MutableStateFlow("")
    val selectedDirectDate = _selectedDirectDate

    private val _selectedDirectTime = MutableStateFlow("")
    val selectedDirectTime = _selectedDirectTime

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
        MutableSharedFlow<UiState<CreateMeetResponseEntity>>()
    val createMeetState = _createMeetState.asSharedFlow()

    private val _directLocationState =
        MutableSharedFlow<UiState<List<LocationResponseEntity>>>()
    val directLocationState = _directLocationState
        .onSubscription {
            getDirectLocations()
        }.shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    private val _candidateLocationState =
        MutableStateFlow<UiState<List<LocationResponseEntity>>>(UiState.Loading)
    val candidateLocationState = _candidateLocationState
        .onSubscription {
            getCandidateLocations()
        }.shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    private val _selectedThemeState = MutableStateFlow<List<ThemesResponseEntity>>(emptyList())
    val selectedThemeState = _selectedThemeState

    private val _selectedDirectLocationState =
        MutableStateFlow<List<LocationResponseEntity>>(emptyList())
    val selectedDirectLocationState = _selectedDirectLocationState

    private val _selectedCandidateLocationState =
        MutableStateFlow<List<LocationResponseEntity>>(emptyList())
    val selectedCandidateLocationState = _selectedCandidateLocationState

    private val _selectedCandidateLocationDetailState =
        MutableStateFlow<List<LocationResponseEntity>>(emptyList())
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
                .onSuccess {
                    _selectedThemeState.value = data
                    _themesState.value = UiState.Success(data)
                }
                .onErrorDeserialize<List<ThemesResponseEntity>, ErrorResponse> {
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

    fun updateDirectDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _selectedDirectDate.value = formatDateToString(year, monthOfYear, dayOfMonth)
    }

    fun updateDirectTime(hourOfDay: Int, minute: Int) {
        _selectedDirectTime.value = formatTimeToString(hourOfDay, minute)
    }

    fun createMeet() {
        viewModelScope.launch {
            _createMeetState.emit(UiState.Loading)

            val createMeetRequestEntity = buildCreateMeetRequestEntity()
            postNewMeetCreateUseCase(createMeetRequestEntity)
                .suspendOnSuccess {
                    _createMeetState.emit(UiState.Success(data))
                }
                .onErrorDeserialize<CreateMeetResponseEntity, ErrorResponse> {
                    launch {
                        _createMeetState.emit(UiState.Failure(it.message))
                    }
                }
        }
    }

    private fun buildCreateMeetRequestEntity(): CreateMeetRequestEntity {
        val placeInfoList = if (_selectedTabPlaceIndex.value == TAB_ADD_CANDIDATE) {
            _selectedCandidateLocationState.value
        } else {
            _selectedDirectLocationState.value
        }

        val voteDate = _selectedCalendarDates.value.takeIf {
            _selectedTabTimeIndex.value == TAB_ADD_CANDIDATE
        }?.let {
            CreateMeetRequestEntity.VoteDate(
                startVoteDate = LocalDate.of(it.first().year, it.first().month + 1, it.first().day)
                    .toString(),
                endVoteDate = LocalDate.of(it.last().year, it.last().month + 1, it.last().day)
                    .toString()
            )
        }

        val meetTime = _selectedTabTimeIndex.value.takeIf {
            it == TAB_DIRECT_INPUT
        }?.let {
            "${_selectedDirectDate.value} ${formatTimeTo24Hour(_selectedDirectTime.value)}"
        }

        return CreateMeetRequestEntity(
            meetTitle = _textLength20State.value,
            description = _textLength80State.value,
            meetThemeId = _selectedThemeState.value.find { it.isSelected }!!.themeId,
            confirmPlace = _selectedTabPlaceIndex.value == TAB_DIRECT_INPUT,
            placeInfo = placeInfoList.map {
                CreateMeetRequestEntity.PlaceInfo(
                    title = it.placeInfoEntity.title,
                    link = it.placeInfoEntity.link,
                    category = it.placeInfoEntity.category,
                    description = it.placeInfoEntity.description,
                    telephone = it.placeInfoEntity.telephone,
                    address = it.placeInfoEntity.address,
                    roadAddress = it.placeInfoEntity.roadAddress,
                    mapx = it.placeInfoEntity.mapx,
                    mapy = it.placeInfoEntity.mapy
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
                            .onErrorDeserialize<Flow<List<LocationResponseEntity>>, ErrorResponse> { errorResponse ->
                                launch {
                                    _directLocationState.emit(UiState.Failure(errorResponse.message))
                                }
                            }
                            .getOrElse {
                                flowOf(emptyList())
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

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun getCandidateLocations() {
        viewModelScope.launch {
            _searchQueryState
                .debounce(SEARCH_QUERY_DEBOUNCE_DELAY)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isNotBlank()) {
                        _candidateLocationState.emit(UiState.Loading)

                        getLocationListUseCase(query)
                            .onErrorDeserialize<Flow<List<LocationResponseEntity>>, ErrorResponse> { errorResponse ->
                                launch {
                                    _candidateLocationState.emit(UiState.Failure(errorResponse.message))
                                }
                            }
                            .getOrElse {
                                flowOf(emptyList())
                            }
                    } else {
                        flowOf(emptyList())
                    }
                }
                .collect { result ->
                    _selectedCandidateLocationDetailState.emit(result)
                    _candidateLocationState.emit(UiState.Success(result))
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
        val selectedLocation = _selectedCandidateLocationDetailState.value.find { it.isSelected }

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
        _selectedDirectDate,
        _selectedDirectTime
    ) { tabIndex, candidateDates, directDate, directTime ->
        when (tabIndex) {
            TAB_ADD_CANDIDATE -> candidateDates.isNotEmpty()
            TAB_DIRECT_INPUT -> directDate.isNotEmpty() && directTime.isNotEmpty()
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

    fun resetSelectedCandidateLocations() {
        _selectedCandidateLocationDetailState.value =
            _selectedCandidateLocationDetailState.value.map {
                it.copy(isSelected = false)
            }
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
        _selectedDirectDate.value = ""
        _selectedDirectTime.value = ""
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