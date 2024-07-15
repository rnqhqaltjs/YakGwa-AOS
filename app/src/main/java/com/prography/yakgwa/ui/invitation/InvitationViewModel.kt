package com.prography.yakgwa.ui.invitation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.model.response.MeetDetailResponseEntity.MeetInfo
import com.prography.domain.usecase.GetMeetInformationDetailUseCase
import com.prography.domain.usecase.PostMeetParticipantUseCase
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
class InvitationViewModel @Inject constructor(
    private val postMeetParticipantUseCase: PostMeetParticipantUseCase,
    private val getMeetInformationDetailUseCase: GetMeetInformationDetailUseCase,
    private val localStorage: YakGwaLocalDataSource
) : ViewModel() {

    private val _detailMeetState =
        MutableStateFlow<UiState<MeetDetailResponseEntity>>(UiState.Loading)
    val detailMeetState = _detailMeetState.asStateFlow()

    private val _participantMeetState =
        MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val participantMeetState = _participantMeetState.asStateFlow()

    private val _meetInfoState = MutableStateFlow<MeetInfo?>(null)
    val meetInfoState = _meetInfoState.asStateFlow()

    fun getMeetInformationDetail(meetId: Int) {
        _detailMeetState.value = UiState.Loading

        viewModelScope.launch {
            getMeetInformationDetailUseCase(meetId)
                .onSuccess {
                    _meetInfoState.value = it.meetInfo
                    _detailMeetState.value = UiState.Success(it)
                }
                .onFailure {
                    _detailMeetState.value = UiState.Failure(it.message)
                }
        }
    }

    fun participantMeet(meetId: Int) {
        _participantMeetState.value = UiState.Loading

        viewModelScope.launch {
            val userId = userId()

            postMeetParticipantUseCase(userId, meetId)
                .onSuccess {
                    _participantMeetState.value = UiState.Success(it)
                }
                .onFailure {
                    _participantMeetState.value = UiState.Failure(it.message)
                }
        }
    }

    suspend fun userId(): Int {
        return withContext(Dispatchers.IO) {
            localStorage.userId.first()
        }
    }
}