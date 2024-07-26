package com.prography.yakgwa.ui.promiseHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.domain.model.response.PromiseHistoryResponseEntity
import com.prography.domain.usecase.GetPromiseHistoryListUseCase
import com.prography.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseHistoryViewModel @Inject constructor(
    private val getPromiseHistoryListUseCase: GetPromiseHistoryListUseCase
) : ViewModel() {
    private val _promiseHistoryState =
        MutableStateFlow<UiState<List<PromiseHistoryResponseEntity>>>(UiState.Loading)
    val promiseHistoryState = _promiseHistoryState
        .onSubscription {
            getPromiseHistory()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    private fun getPromiseHistory() {
        _promiseHistoryState.value = UiState.Loading

        viewModelScope.launch {
            getPromiseHistoryListUseCase()
                .onSuccess {
                    _promiseHistoryState.value = UiState.Success(it)
                }.onFailure {
                    _promiseHistoryState.value = UiState.Failure(it.message)
                }
        }
    }
}