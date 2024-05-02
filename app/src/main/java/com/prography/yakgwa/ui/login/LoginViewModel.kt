package com.prography.yakgwa.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.model.request.RequestAuthDto
import com.prography.domain.model.request.AuthRequestEntity
import com.prography.domain.repository.AuthRepository
import com.prography.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val loginState get() = _loginState.asStateFlow()

    fun login(kakaoAccessToken: String) {
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            authRepository.postLogin(
                kakaoAccessToken,
                AuthRequestEntity("KAKAO")
            ).onSuccess {
                _loginState.value = UiState.Success(Unit)
            }.onFailure {
                _loginState.value = UiState.Failure(it.message)
            }
        }
    }
}