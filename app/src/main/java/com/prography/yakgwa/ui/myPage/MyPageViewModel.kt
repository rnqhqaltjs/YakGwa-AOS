package com.prography.yakgwa.ui.myPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.domain.repository.AuthRepository
import com.prography.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val localStorage: YakGwaLocalDataSource
) : ViewModel() {

    private val _logoutState = MutableSharedFlow<UiState<Unit>>()
    val logoutState = _logoutState.asSharedFlow()

    fun logout() {
        viewModelScope.launch {
            _logoutState.emit(UiState.Loading)

            authRepository.logout()
                .onSuccess {
                    _logoutState.emit(UiState.Success(Unit))
                    localStorage.clear()
                }.onFailure {
                    _logoutState.emit(UiState.Failure(it.message))
                }
        }
    }
}