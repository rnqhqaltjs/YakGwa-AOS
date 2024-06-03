package com.prography.yakgwa.ui.createPromise

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.domain.model.request.CreateMeetRequestEntity
import com.prography.domain.model.response.LocationResponseEntity
import com.prography.domain.model.response.ThemesResponseEntity
import com.prography.domain.usecase.GetLocationListUseCase
import com.prography.domain.usecase.GetThemeListUseCase
import com.prography.domain.usecase.PostNewMeetCreateUseCase
import com.prography.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CreatePromiseViewModel @Inject constructor(
    private val getThemeListUseCase: GetThemeListUseCase,
    private val postNewMeetCreateUseCase: PostNewMeetCreateUseCase,
    private val localStorage: YakGwaLocalDataSource,
    private val getLocationListUseCase: GetLocationListUseCase
) : ViewModel() {

    private val _textLength20State = MutableStateFlow(0)
    val textLength20State = _textLength20State.asStateFlow()

    private val _textLength80State = MutableStateFlow(0)
    val textLength80State = _textLength80State.asStateFlow()

    private val _themesState =
        MutableStateFlow<UiState<List<ThemesResponseEntity>>>(UiState.Loading)
    val themesState = _themesState.asStateFlow()

    private val _selectedStartDate = MutableStateFlow<String?>(null)
    val selectedStartDate = _selectedStartDate.asStateFlow()

    private val _selectedEndDate = MutableStateFlow<String?>(null)
    val selectedEndDate = _selectedEndDate.asStateFlow()

    private val _selectedStartTime = MutableStateFlow<String?>(null)
    val selectedStartTime = _selectedStartTime.asStateFlow()

    private val _selectedEndTime = MutableStateFlow<String?>(null)
    val selectedEndTime = _selectedEndTime.asStateFlow()

    private val _createMeetState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val createMeetState = _createMeetState.asStateFlow()

    private val _locationState =
        MutableStateFlow<UiState<List<LocationResponseEntity>>>(UiState.Loading)
    val locationState = _locationState.asStateFlow()

    private val _searchQueryState = MutableStateFlow("")
    val searchQueryState = _searchQueryState.asStateFlow()

    init {
        getThemes()
        getLocations()
    }

    private fun getThemes() {
        _themesState.value = UiState.Loading

        viewModelScope.launch {
            getThemeListUseCase()
                .onSuccess {
                    _themesState.value = UiState.Success(it)
                }
                .onFailure {
                    UiState.Failure(it.message)
                }
        }
    }

    fun onTextChanged20(text: String) {
        _textLength20State.value = text.length
    }

    fun onTextChanged80(text: String) {
        _textLength80State.value = text.length
    }

    fun updateStartDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _selectedStartDate.value = formatDate(year, monthOfYear, dayOfMonth)
    }

    fun updateEndDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _selectedEndDate.value = formatDate(year, monthOfYear, dayOfMonth)
    }

    fun updateStartTime(hourOfDay: Int, minute: Int) {
        _selectedStartTime.value = formatTime(hourOfDay, minute)
    }

    fun updateEndTime(hourOfDay: Int, minute: Int) {
        _selectedEndTime.value = formatTime(hourOfDay, minute)
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val date = LocalDate.of(year, month, day)
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return date.format(dateFormat)
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val time = LocalTime.of(hour, minute)
        val timeFormat = DateTimeFormatter.ofPattern("a hh:mm")
        return time.format(timeFormat)
    }

    fun createMeet(createMeetRequestEntity: CreateMeetRequestEntity) {
        _createMeetState.value = UiState.Loading

        viewModelScope.launch {
            postNewMeetCreateUseCase(userId(), createMeetRequestEntity)
                .onSuccess {
                    _createMeetState.value = UiState.Success(Unit)
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
                .debounce(SEARCH_QUERY_DELAY_TIME)
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

    companion object {
        const val SEARCH_QUERY_DELAY_TIME = 300L
    }
}