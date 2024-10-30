package com.yomo.yakgwa.ui.voteResult

import android.graphics.Bitmap
import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.serialization.onErrorDeserialize
import com.yomo.data.utils.ErrorResponse
import com.yomo.domain.model.request.ConfirmPlaceRequestEntity
import com.yomo.domain.model.request.ConfirmTimeRequestEntity
import com.yomo.domain.model.response.MeetDetailResponseEntity
import com.yomo.domain.model.response.TimeCandidateResponseEntity
import com.yomo.domain.model.response.TimeCandidateResponseEntity.TimeInfo
import com.yomo.domain.model.response.VotePlaceResponseEntity
import com.yomo.domain.repository.PlaceRepository
import com.yomo.domain.usecase.GetMeetInformationDetailUseCase
import com.yomo.domain.usecase.GetStaticMapUseCase
import com.yomo.domain.usecase.GetUserVotePlaceListUseCase
import com.yomo.domain.usecase.GetVoteTimeCandidateInfoUseCase
import com.yomo.domain.usecase.PatchConfirmMeetPlaceUseCase
import com.yomo.domain.usecase.PatchConfirmMeetTimeUseCase
import com.yomo.yakgwa.model.NaviModel
import com.yomo.yakgwa.type.NaviType
import com.yomo.yakgwa.util.UiState
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
    savedStateHandle: SavedStateHandle,
    private val getVoteTimeCandidateInfoUseCase: GetVoteTimeCandidateInfoUseCase,
    private val getUserVoteInfoUseCase: GetUserVotePlaceListUseCase,
    private val getMeetInformationDetailUseCase: GetMeetInformationDetailUseCase,
    private val patchConfirmMeetTimeUseCase: PatchConfirmMeetTimeUseCase,
    private val patchConfirmMeetPlaceUseCase: PatchConfirmMeetPlaceUseCase,
    private val getStaticMapUseCase: GetStaticMapUseCase,
    private val placeRepository: PlaceRepository
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

    private val _selectedConfirmPlaceState =
        MutableStateFlow<List<VotePlaceResponseEntity.PlaceInfos>>(emptyList())
    val selectedConfirmPlaceState = _selectedConfirmPlaceState

    private val _selectedConfirmTimeState = MutableStateFlow<List<TimeInfo>>(emptyList())
    val selectedConfirmTimeState = _selectedConfirmTimeState

    private val _naviInfoState = MutableStateFlow<NaviModel?>(null)

    private val _naviActionState = MutableSharedFlow<Pair<NaviType, NaviModel>>()
    val naviActionState = _naviActionState.asSharedFlow()

    private val _staticMapState = MutableStateFlow<Bitmap?>(null)
    val staticMapState = _staticMapState.asStateFlow()

    private val _participantInfo =
        MutableStateFlow<Array<MeetDetailResponseEntity.ParticipantInfo>>(emptyArray())
    val participantInfo = _participantInfo

    fun getVoteTimeCandidate() {
        _timeCandidateState.value = UiState.Loading

        viewModelScope.launch {
            getVoteTimeCandidateInfoUseCase(meetId)
                .onSuccess {
                    _timeCandidateState.value = UiState.Success(data)
                    _selectedConfirmTimeState.value = data.selectFirstTime()
                }
                .onErrorDeserialize<TimeCandidateResponseEntity, ErrorResponse> {
                    _timeCandidateState.value = UiState.Failure(it.message)
                }
        }
    }

    fun getUserVotePlace() {
        _votePlaceInfoState.value = UiState.Loading

        viewModelScope.launch {
            getUserVoteInfoUseCase(meetId)
                .onSuccess {
                    _votePlaceInfoState.value = UiState.Success(data)
                    _selectedConfirmPlaceState.value = data.selectFirstPlace()
                    _naviInfoState.value = data.placeInfos?.firstOrNull()?.let { placeInfo ->
                        NaviModel(
                            placeInfo.mapX,
                            placeInfo.mapY,
                            placeInfo.roadAddress
                        )
                    }
                }
                .onErrorDeserialize<VotePlaceResponseEntity, ErrorResponse> {
                    _votePlaceInfoState.value = UiState.Failure(it.message)
                }
        }
    }

    private fun getMeetInformationDetail() {
        _detailMeetState.value = UiState.Loading

        viewModelScope.launch {
            getMeetInformationDetailUseCase(meetId)
                .onSuccess {
                    _detailMeetState.value = UiState.Success(data)
                }
                .onErrorDeserialize<MeetDetailResponseEntity, ErrorResponse> {
                    _detailMeetState.value = UiState.Failure(it.message)
                }
        }
    }

    fun confirmMeetTime() {
        _confirmTimeState.value = UiState.Loading

        val confirmTimeRequestEntity =
            ConfirmTimeRequestEntity(_selectedConfirmTimeState.value.find { it.isSelected }!!.timeId)

        viewModelScope.launch {
            patchConfirmMeetTimeUseCase(meetId, confirmTimeRequestEntity)
                .onSuccess {
                    _confirmTimeState.value = UiState.Success(data)
                }
                .onErrorDeserialize<String, ErrorResponse> {
                    _confirmTimeState.value = UiState.Failure(it.message)
                }
        }
    }

    fun confirmMeetPlace() {
        _confirmPlaceState.value = UiState.Loading

        val confirmPlaceRequestEntity =
            ConfirmPlaceRequestEntity(_selectedConfirmPlaceState.value.find { it.isSelected }!!.placeSlotId)

        viewModelScope.launch {
            patchConfirmMeetPlaceUseCase(meetId, confirmPlaceRequestEntity)
                .onSuccess {
                    _confirmPlaceState.value = UiState.Success(data)
                }.onErrorDeserialize<String, ErrorResponse> {
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

    fun setParticipantInfo(participants: List<MeetDetailResponseEntity.ParticipantInfo>) {
        _participantInfo.value = participants.toTypedArray()
    }

    fun getStaticMap(width: Int, height: Int, center: String, level: Int, markers: String) {
        viewModelScope.launch {
            getStaticMapUseCase(width, height, center, level, markers)
                .onSuccess {
                    _staticMapState.value = it
                }
        }
    }

    suspend fun geoCoding(address: String): Location {
        return try {
            placeRepository.geoCoding(address)
        } catch (e: Exception) {
            e.printStackTrace()
            Location("").apply {
                latitude = 0.0
                longitude = 0.0
            }
        }
    }

    companion object {
        const val MEET_ID = "meetId"
        const val INVALID_MEET_ID = -1
    }
}