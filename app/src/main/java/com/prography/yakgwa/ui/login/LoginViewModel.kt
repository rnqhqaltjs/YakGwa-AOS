package com.prography.yakgwa.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.domain.model.request.AuthRequestEntity
import com.prography.domain.repository.AuthRepository
import com.prography.yakgwa.type.LoginType
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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val localStorage: YakGwaLocalDataSource
) : ViewModel() {
    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val loginState = _loginState.asStateFlow()

    fun login(kakaoAccessToken: String) {
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            authRepository.postLogin(
                HEADER_BEARER + kakaoAccessToken,
                AuthRequestEntity(LoginType.KAKAO.name, getDeviceToken())
            ).onSuccess { authEntity ->
                with(localStorage) {
                    saveIsLogin(true)
                    saveAccessToken(HEADER_BEARER + authEntity.accessToken)
                    saveRefreshToken(HEADER_BEARER + authEntity.refreshToken)
                }
                _loginState.value = UiState.Success(Unit)
            }.onFailure {
                _loginState.value = UiState.Failure(it.message)
            }
        }
    }

    private suspend fun getDeviceToken(): String {
        return withContext(Dispatchers.IO) {
            localStorage.deviceToken.first()
        }
    }

    companion object {
        private const val HEADER_BEARER = "Bearer "
    }
}