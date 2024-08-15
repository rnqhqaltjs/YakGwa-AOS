package com.prography.yakgwa.ui.myPage

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.ErrorResponse
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.domain.model.response.UserInfoResponseEntity
import com.prography.domain.repository.AuthRepository
import com.prography.domain.usecase.GetUserInformationUseCase
import com.prography.domain.usecase.PatchUpdateUserImageUseCase
import com.prography.yakgwa.util.UiState
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.serialization.onErrorDeserialize
import com.skydoves.sandwich.suspendOnSuccess
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
    private val getUserInformationUseCase: GetUserInformationUseCase,
    private val patchUpdateUserImageUseCase: PatchUpdateUserImageUseCase
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

    private val _userImageState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val userImageState = _userImageState.asStateFlow()

    fun logout() {
        _logoutState.value = UiState.Loading

        viewModelScope.launch {
            authRepository.logout()
                .suspendOnSuccess {
                    _logoutState.value = UiState.Success(data)
                    localStorage.clear()
                }
                .onErrorDeserialize<Unit, ErrorResponse> {
                    _logoutState.value = UiState.Failure(it.message)
                }
        }
    }

    private fun getUserInfo() {
        _userInfoState.value = UiState.Loading

        viewModelScope.launch {
            getUserInformationUseCase()
                .onSuccess {
                    _userInfoState.value = UiState.Success(data)
                }
                .onErrorDeserialize<UserInfoResponseEntity, ErrorResponse> {
                    _userInfoState.value = UiState.Failure(it.message)
                }
        }
    }

    fun updateUserImage(imageUri: Uri) {
        _userImageState.value = UiState.Loading

        viewModelScope.launch {
            patchUpdateUserImageUseCase(imageUri)
                .onSuccess {
                    _userImageState.value = UiState.Success(data)
                }
                .onErrorDeserialize<Unit, ErrorResponse> {
                    _userImageState.value = UiState.Failure(it.message)
                }
        }
    }
}