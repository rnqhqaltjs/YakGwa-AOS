package com.prography.yakgwa.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.domain.repository.AuthRepository
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
    private val authRepository: AuthRepository,
    private val localStorage: YakGwaLocalDataSource
) : ViewModel() {

    private val _logoutState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val logoutState = _logoutState.asStateFlow()

    fun logout() {
        _logoutState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = withContext(Dispatchers.IO) {
                localStorage.accessToken.first()
            }
            authRepository.logout(accessToken)
                .onSuccess {
                    _logoutState.value = UiState.Success(Unit)
                    localStorage.clear()
                }.onFailure { throwable ->
                    _logoutState.value = UiState.Failure(throwable.message)
                }
        }
    }

}