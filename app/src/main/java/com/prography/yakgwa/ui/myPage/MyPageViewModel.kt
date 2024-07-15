package com.prography.yakgwa.ui.myPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.domain.repository.AuthRepository
import com.prography.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val localStorage: YakGwaLocalDataSource
) : ViewModel() {

    private val _logoutState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val logoutState = _logoutState.asStateFlow()

    fun logout() {
        _logoutState.value = UiState.Loading

        viewModelScope.launch {
            authRepository.logout()
                .onSuccess {
                    _logoutState.value = UiState.Success(Unit)
                    localStorage.clear()
                }.onFailure {
                    _logoutState.value = UiState.Failure(it.message)
                }
        }
    }
}