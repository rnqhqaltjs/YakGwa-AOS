package com.prography.yakgwa.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.usecase.GetParticipantMeetListUseCase
import com.prography.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getParticipantMeetListUseCase: GetParticipantMeetListUseCase
) : ViewModel() {

    private val _meetsState = MutableStateFlow<UiState<List<MeetsResponseEntity>>>(UiState.Loading)
    val meetsState = _meetsState
        .onSubscription {
            getParticipantMeets()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    private fun getParticipantMeets() {
        _meetsState.value = UiState.Loading

        viewModelScope.launch {
            getParticipantMeetListUseCase()
                .onSuccess {
                    _meetsState.value = UiState.Success(it)
                }.onFailure {
                    _meetsState.value = UiState.Failure(it.message)
                }
        }
    }
}