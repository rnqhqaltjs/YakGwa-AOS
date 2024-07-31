package com.prography.yakgwa.ui.invitation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.model.response.MeetDetailResponseEntity.MeetInfo
import com.prography.domain.model.response.ParticipantMeetResponseEntity
import com.prography.domain.model.response.PlaceCandidateResponseEntity
import com.prography.domain.model.response.TimeCandidateResponseEntity
import com.prography.domain.model.response.VotePlaceResponseEntity
import com.prography.domain.usecase.GetMeetInformationDetailUseCase
import com.prography.domain.usecase.GetPlaceCandidateInfoUseCase
import com.prography.domain.usecase.GetUserVotePlaceListUseCase
import com.prography.domain.usecase.GetVoteTimeCandidateInfoUseCase
import com.prography.domain.usecase.PostParticipantMeetUseCase
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
class InvitationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val postParticipantMeetUseCase: PostParticipantMeetUseCase,
    private val getMeetInformationDetailUseCase: GetMeetInformationDetailUseCase,
    private val getVoteTimeCandidateInfoUseCase: GetVoteTimeCandidateInfoUseCase,
    private val getUserVoteInfoUseCase: GetUserVotePlaceListUseCase,
    private val getPlaceCandidateInfoUseCase: GetPlaceCandidateInfoUseCase,
) : ViewModel() {
    val meetId: Int
        get() {
            return when (val value = savedStateHandle.get<Any>(MEET_ID)) {
                is Int -> value
                is String -> value.toIntOrNull() ?: INVALID_MEET_ID
                else -> INVALID_MEET_ID
            }
        }

    private val _detailMeetState =
        MutableStateFlow<UiState<MeetDetailResponseEntity>>(UiState.Loading)
    val detailMeetState = _detailMeetState
        .onSubscription {
            getMeetInformationDetail()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    private val _participantMeetState =
        MutableStateFlow<UiState<ParticipantMeetResponseEntity>>(UiState.Loading)
    val participantMeetState = _participantMeetState.asStateFlow()

    private val _timeCandidateState =
        MutableStateFlow<UiState<TimeCandidateResponseEntity>>(UiState.Loading)
    val timeCandidateState = _timeCandidateState
        .onSubscription {
            getVoteTimeCandidate()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    private val _meetInfoState = MutableStateFlow<MeetInfo?>(null)
    val meetInfoState = _meetInfoState

    private val _votePlaceInfoState =
        MutableStateFlow<UiState<VotePlaceResponseEntity>>(UiState.Loading)
    val votePlaceInfoState = _votePlaceInfoState
        .onSubscription {
            getUserVotePlace()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    private val _placeCandidateState =
        MutableStateFlow<UiState<List<PlaceCandidateResponseEntity>>>(UiState.Loading)
    val placeCandidateState = _placeCandidateState
        .onSubscription {
            getVotePlaceCandidate()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    private val _participantInfo =
        MutableStateFlow<Array<MeetDetailResponseEntity.ParticipantInfo>>(emptyArray())
    val participantInfo = _participantInfo

    private fun getMeetInformationDetail() {
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

    fun participantMeet() {
        _participantMeetState.value = UiState.Loading

        viewModelScope.launch {
            postParticipantMeetUseCase(meetId)
                .onSuccess {
                    _participantMeetState.value = UiState.Success(it)
                }
                .onFailure {
                    _participantMeetState.value = UiState.Failure(it.message)
                }
        }
    }

    private fun getVoteTimeCandidate() {
        _timeCandidateState.value = UiState.Loading

        viewModelScope.launch {
            getVoteTimeCandidateInfoUseCase(meetId)
                .onSuccess {
                    _timeCandidateState.value = UiState.Success(it)
                }
                .onFailure {
                    _timeCandidateState.value = UiState.Failure(it.message)
                }
        }
    }

    private fun getUserVotePlace() {
        _votePlaceInfoState.value = UiState.Loading

        viewModelScope.launch {
            getUserVoteInfoUseCase(meetId)
                .onSuccess {
                    _votePlaceInfoState.value = UiState.Success(it)
                }
                .onFailure {
                    _votePlaceInfoState.value = UiState.Failure(it.message)
                }
        }
    }

    private fun getVotePlaceCandidate() {
        _placeCandidateState.value = UiState.Loading

        viewModelScope.launch {
            getPlaceCandidateInfoUseCase(meetId)
                .onSuccess {
                    _placeCandidateState.value = UiState.Success(it)
                }
                .onFailure {
                    _placeCandidateState.value = UiState.Failure(it.message)
                }
        }
    }

    fun setParticipantInfo(participants: List<MeetDetailResponseEntity.ParticipantInfo>) {
        _participantInfo.value = participants.toTypedArray()
    }

    companion object {
        const val MEET_ID = "meetId"
        const val INVALID_MEET_ID = -1
    }
}