package com.prography.yakgwa.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.ErrorResponse
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.domain.model.request.AuthRequestEntity
import com.prography.domain.model.response.AuthResponseEntity
import com.prography.domain.usecase.PostUserLoginUseCase
import com.prography.yakgwa.type.LoginType
import com.prography.yakgwa.util.UiState
import com.skydoves.sandwich.retrofit.serialization.onErrorDeserialize
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postUserLoginUseCase: PostUserLoginUseCase,
    private val localStorage: YakGwaLocalDataSource
) : ViewModel() {
    private val _loginState = MutableSharedFlow<UiState<Unit>>()
    val loginState = _loginState.asSharedFlow()

    fun login(kakaoAccessToken: String) {
        viewModelScope.launch {
            _loginState.emit(UiState.Loading)

            postUserLoginUseCase(
                HEADER_BEARER + kakaoAccessToken,
                AuthRequestEntity(LoginType.KAKAO.name, getDeviceToken())
            ).suspendOnSuccess {
                with(localStorage) {
                    saveIsLogin(true)
                    saveAccessToken(HEADER_BEARER + data.accessToken)
                    saveRefreshToken(HEADER_BEARER + data.refreshToken)
                }
                _loginState.emit(UiState.Success(Unit))
            }.onErrorDeserialize<AuthResponseEntity, ErrorResponse> {
                launch {
                    _loginState.emit(UiState.Failure(it.message))
                }
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