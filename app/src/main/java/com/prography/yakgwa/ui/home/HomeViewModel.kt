package com.prography.yakgwa.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.usecase.GetParticipantMeetListUseCase
import com.prography.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getParticipantMeetListUseCase: GetParticipantMeetListUseCase
) : ViewModel() {
    
    private val _participantMeetState =
        MutableStateFlow<UiState<List<MeetsResponseEntity>>>(UiState.Loading)
    val participantMeetState = _participantMeetState.asStateFlow()

    fun getParticipantMeets(userId: Int) {
        viewModelScope.launch {
            _participantMeetState.emit(UiState.Loading)

            runCatching {
                getParticipantMeetListUseCase(userId).collect {
                    _participantMeetState.emit(UiState.Success(it))
                }
            }.onFailure {
                _participantMeetState.emit(UiState.Failure(it.message))
            }
        }
    }

}