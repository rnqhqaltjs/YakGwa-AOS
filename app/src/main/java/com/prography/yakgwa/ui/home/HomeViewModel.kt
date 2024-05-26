package com.prography.yakgwa.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.domain.repository.AuthRepository
import com.prography.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val localStorage: YakGwaLocalDataSource
) : ViewModel() {

    private val _logoutState = MutableSharedFlow<UiState<Unit>>(replay = 1)
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

    suspend fun isLogin(): String {
        return withContext(Dispatchers.IO) {
            localStorage.accessToken.first()
        }
    }

}