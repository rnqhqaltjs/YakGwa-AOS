package com.yomo.yakgwa.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.serialization.onErrorDeserialize
import com.yomo.data.utils.ErrorResponse
import com.yomo.domain.model.response.MeetsResponseEntity
import com.yomo.domain.usecase.GetParticipantMeetListUseCase
import com.yomo.yakgwa.util.UiState
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
                    _meetsState.value = UiState.Success(data)
                }
                .onErrorDeserialize<List<MeetsResponseEntity>, ErrorResponse> {
                    _meetsState.value = UiState.Failure(it.message)
                }
        }
    }
}