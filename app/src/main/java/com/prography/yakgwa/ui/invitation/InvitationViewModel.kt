package com.prography.yakgwa.ui.invitation

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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationViewModel @Inject constructor(
    private val postParticipantMeetUseCase: PostParticipantMeetUseCase,
    private val getMeetInformationDetailUseCase: GetMeetInformationDetailUseCase,
    private val getVoteTimeCandidateInfoUseCase: GetVoteTimeCandidateInfoUseCase,
    private val getUserVoteInfoUseCase: GetUserVotePlaceListUseCase,
    private val getPlaceCandidateInfoUseCase: GetPlaceCandidateInfoUseCase,
) : ViewModel() {

    private val _detailMeetState =
        MutableStateFlow<UiState<MeetDetailResponseEntity>>(UiState.Loading)
    val detailMeetState = _detailMeetState.asStateFlow()

    private val _participantMeetState =
        MutableStateFlow<UiState<ParticipantMeetResponseEntity>>(UiState.Loading)
    val participantMeetState = _participantMeetState.asStateFlow()

    private val _timeCandidateState =
        MutableStateFlow<UiState<TimeCandidateResponseEntity>>(UiState.Loading)
    val timeCandidateState = _timeCandidateState.asStateFlow()

    private val _meetInfoState = MutableStateFlow<MeetInfo?>(null)
    val meetInfoState = _meetInfoState.asStateFlow()

    private val _votePlaceInfoState =
        MutableStateFlow<UiState<VotePlaceResponseEntity>>(UiState.Loading)
    val votePlaceInfoState = _votePlaceInfoState.asStateFlow()

    private val _placeCandidateState =
        MutableStateFlow<UiState<List<PlaceCandidateResponseEntity>>>(UiState.Loading)
    val placeCandidateState = _placeCandidateState.asStateFlow()

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
            postParticipantMeetUseCase(meetId)
                .onSuccess {
                    _participantMeetState.value = UiState.Success(it)
                }
                .onFailure {
                    _participantMeetState.value = UiState.Failure(it.message)
                }
        }
    }

    fun getVoteTimeCandidate(meetId: Int) {
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

    fun getUserVotePlace(meetId: Int) {
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

    fun getVotePlaceCandidate(meetId: Int) {
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
}