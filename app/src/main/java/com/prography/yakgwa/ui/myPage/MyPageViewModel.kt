package com.prography.yakgwa.ui.myPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.domain.model.response.UserInfoResponseEntity
import com.prography.domain.repository.AuthRepository
import com.prography.domain.usecase.GetUserInformationUseCase
import com.prography.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val localStorage: YakGwaLocalDataSource,
    private val getUserInformationUseCase: GetUserInformationUseCase
) : ViewModel() {

    private val _logoutState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val logoutState = _logoutState.asStateFlow()

    private val _userInfoState = MutableStateFlow<UiState<UserInfoResponseEntity>>(UiState.Loading)
    val userInfoState = _userInfoState
        .onSubscription {
            getUserInfo()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

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

    private fun getUserInfo() {
        _userInfoState.value = UiState.Loading

        viewModelScope.launch {
            getUserInformationUseCase()
                .onSuccess {
                    _userInfoState.value = UiState.Success(it)
                }.onFailure {
                    _userInfoState.value = UiState.Failure(it.message)
                }
        }
    }
}