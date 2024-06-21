package com.prography.yakgwa.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.domain.usecase.GetParticipantMeetListUseCase
import com.prography.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getParticipantMeetListUseCase: GetParticipantMeetListUseCase,
    private val localStorage: YakGwaLocalDataSource
) : ViewModel() {

    private val _meetsState = MutableStateFlow<UiState<List<MeetsResponseEntity>>>(UiState.Loading)
    val meetsState = _meetsState.asStateFlow()

    init {
        getParticipantMeets()
    }
    
    private fun getParticipantMeets() {
        _meetsState.value = UiState.Loading

        viewModelScope.launch {
            val userId = userId()

            getParticipantMeetListUseCase(userId)
                .onSuccess {
                    _meetsState.value = UiState.Success(it)
                }.onFailure {
                    _meetsState.value = UiState.Failure(it.message)
                }
        }
    }

    private suspend fun userId(): Int {
        return withContext(Dispatchers.IO) {
            localStorage.userId.first()
        }
    }
}