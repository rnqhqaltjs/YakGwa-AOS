package com.prography.yakgwa.ui.voteResult

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.domain.model.request.ConfirmPlaceRequestEntity
import com.prography.domain.model.request.ConfirmTimeRequestEntity
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.domain.model.response.TimeCandidateResponseEntity
import com.prography.domain.model.response.VotePlaceResponseEntity
import com.prography.domain.usecase.GetMeetInformationDetailUseCase
import com.prography.domain.usecase.GetUserVotePlaceListUseCase
import com.prography.domain.usecase.GetVoteTimeCandidateInfoUseCase
import com.prography.domain.usecase.PatchConfirmMeetPlaceUseCase
import com.prography.domain.usecase.PatchConfirmMeetTimeUseCase
import com.prography.yakgwa.model.ConfirmPlaceModel
import com.prography.yakgwa.model.ConfirmTimeModel
import com.prography.yakgwa.model.NaviModel
import com.prography.yakgwa.type.NaviType
import com.prography.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoteResultViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getVoteTimeCandidateInfoUseCase: GetVoteTimeCandidateInfoUseCase,
    private val getUserVoteInfoUseCase: GetUserVotePlaceListUseCase,
    private val getMeetInformationDetailUseCase: GetMeetInformationDetailUseCase,
    private val patchConfirmMeetTimeUseCase: PatchConfirmMeetTimeUseCase,
    private val patchConfirmMeetPlaceUseCase: PatchConfirmMeetPlaceUseCase,
) : ViewModel() {
    private var meetId = savedStateHandle.get<Int>(MEET_ID) ?: INVALID_MEET_ID

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

    private val _confirmTimeState = MutableStateFlow<UiState<String>>(UiState.Loading)
    val confirmTimeState = _confirmTimeState.asStateFlow()

    private val _confirmPlaceState = MutableStateFlow<UiState<String>>(UiState.Loading)
    val confirmPlaceState = _confirmPlaceState.asStateFlow()

    private val _selectedConfirmPlaceState = MutableStateFlow<List<ConfirmPlaceModel>>(emptyList())
    val selectedConfirmPlaceState = _selectedConfirmPlaceState

    private val _selectedConfirmTimeState = MutableStateFlow<List<ConfirmTimeModel>>(emptyList())
    val selectedConfirmTimeState = _selectedConfirmTimeState

    private val _naviInfoState = MutableStateFlow<NaviModel?>(null)

    private val _naviActionState = MutableSharedFlow<Pair<NaviType, NaviModel>>()
    val naviActionState = _naviActionState.asSharedFlow()

    fun getVoteTimeCandidate() {
        _timeCandidateState.value = UiState.Loading

        viewModelScope.launch {
            getVoteTimeCandidateInfoUseCase(meetId)
                .onSuccess {
                    _timeCandidateState.value = UiState.Success(it)
                    val confirmTimeModels = it.timeInfos?.mapIndexed { index, placeInfo ->
                        ConfirmTimeModel(placeInfo).apply {
                            if (index == 0) {
                                isSelected = true
                            }
                        }
                    } ?: emptyList()

                    _selectedConfirmTimeState.value = confirmTimeModels
                }
                .onFailure {
                    _timeCandidateState.value = UiState.Failure(it.message)
                }
        }
    }

    fun getUserVotePlace() {
        _votePlaceInfoState.value = UiState.Loading

        viewModelScope.launch {
            getUserVoteInfoUseCase(meetId)
                .onSuccess {
                    _votePlaceInfoState.value = UiState.Success(it)
                    val confirmPlaceModels = it.placeInfos?.mapIndexed { index, placeInfo ->
                        ConfirmPlaceModel(placeInfo).apply {
                            if (index == 0) {
                                isSelected = true
                            }
                        }
                    } ?: emptyList()

                    _selectedConfirmPlaceState.value = confirmPlaceModels
                    _naviInfoState.value = it.placeInfos?.firstOrNull()?.let { placeInfo ->
                        NaviModel(
                            placeInfo.mapX,
                            placeInfo.mapY,
                            placeInfo.roadAddress
                        )
                    }
                }
                .onFailure {
                    _votePlaceInfoState.value = UiState.Failure(it.message)
                }
        }
    }

    private fun getMeetInformationDetail() {
        _detailMeetState.value = UiState.Loading

        viewModelScope.launch {
            getMeetInformationDetailUseCase(meetId)
                .onSuccess {
                    _detailMeetState.value = UiState.Success(it)
                }
                .onFailure {
                    _detailMeetState.value = UiState.Failure(it.message)
                }
        }
    }

    fun confirmMeetTime() {
        _confirmTimeState.value = UiState.Loading

        val confirmTimeRequestEntity =
            ConfirmTimeRequestEntity(
                _selectedConfirmTimeState.value
                    .find { it.isSelected }!!
                    .timeInfo.timeId
            )

        viewModelScope.launch {
            patchConfirmMeetTimeUseCase(meetId, confirmTimeRequestEntity)
                .onSuccess {
                    _confirmTimeState.value = UiState.Success(it)
                }.onFailure {
                    _confirmTimeState.value = UiState.Failure(it.message)
                }
        }
    }

    fun confirmMeetPlace() {
        _confirmPlaceState.value = UiState.Loading

        val confirmPlaceRequestEntity =
            ConfirmPlaceRequestEntity(
                _selectedConfirmPlaceState.value
                    .find { it.isSelected }!!
                    .placeInfos.placeSlotId
            )

        viewModelScope.launch {
            patchConfirmMeetPlaceUseCase(meetId, confirmPlaceRequestEntity)
                .onSuccess {
                    _confirmPlaceState.value = UiState.Success(it)
                }.onFailure {
                    _confirmPlaceState.value = UiState.Failure(it.message)
                }
        }
    }

    fun singleConfirmTimeSelection(position: Int) {
        val currentList = _selectedConfirmTimeState.value.mapIndexed { index, item ->
            item.copy(isSelected = index == position)
        }
        _selectedConfirmTimeState.value = currentList
    }

    fun singleConfirmPlaceSelection(position: Int) {
        val currentList = _selectedConfirmPlaceState.value.mapIndexed { index, item ->
            item.copy(isSelected = index == position)
        }
        _selectedConfirmPlaceState.value = currentList
    }

    fun onNaviAction(naviType: NaviType) {
        viewModelScope.launch {
            _naviInfoState.value?.let { naviInfo ->
                _naviActionState.emit(Pair(naviType, naviInfo))
            }
        }
    }

    companion object {
        const val MEET_ID = "meetId"
        const val INVALID_MEET_ID = -1
    }
}