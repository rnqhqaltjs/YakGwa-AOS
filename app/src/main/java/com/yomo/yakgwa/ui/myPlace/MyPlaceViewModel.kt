package com.yomo.yakgwa.ui.myPlace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.serialization.onErrorDeserialize
import com.skydoves.sandwich.suspendOnSuccess
import com.yomo.data.utils.ErrorResponse
import com.yomo.domain.model.request.MyPlaceRequestEntity
import com.yomo.domain.model.response.LocationResponseEntity
import com.yomo.domain.usecase.GetLocationListUseCase
import com.yomo.domain.usecase.PostMyPlaceUseCase
import com.yomo.yakgwa.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPlaceViewModel @Inject constructor(
    private val getLocationListUseCase: GetLocationListUseCase,
    private val postMyPlaceUseCase: PostMyPlaceUseCase
) : ViewModel() {
    private val _locationState =
        MutableStateFlow<UiState<List<LocationResponseEntity>>>(UiState.Loading)
    val locationState = _locationState.asStateFlow()

    private val _myPlaceState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val myPlaceState = _myPlaceState.asStateFlow()

    private val _toggleState = MutableStateFlow<List<LocationResponseEntity>>(emptyList())
    val toggleState = _toggleState

    fun getLocations(query: String) {
        _locationState.value = UiState.Loading

        viewModelScope.launch {
            getLocationListUseCase(query)
                .suspendOnSuccess {
                    data.collect {
                        _locationState.value = UiState.Success(it)
                        _toggleState.value = it
                    }
                }.onErrorDeserialize<Flow<List<LocationResponseEntity>>, ErrorResponse> {
                    _locationState.value = UiState.Failure(it.message)
                }
        }
    }

    fun myPlace(myPlaceRequestEntity: MyPlaceRequestEntity, like: Boolean) {
        _myPlaceState.value = UiState.Loading

        viewModelScope.launch {
            postMyPlaceUseCase(like, myPlaceRequestEntity)
                .onSuccess {
                    _myPlaceState.value = UiState.Success(data)
                    updateToggleState(myPlaceRequestEntity, like)
                }
                .onErrorDeserialize<Boolean, ErrorResponse> {
                    _myPlaceState.value = UiState.Failure(it.message)
                }
        }
    }

    private fun updateToggleState(requestEntity: MyPlaceRequestEntity, like: Boolean) {
        val updatedList = _toggleState.value.map { item ->
            if (item.placeInfoEntity.title == requestEntity.title &&
                item.placeInfoEntity.mapx == requestEntity.mapx &&
                item.placeInfoEntity.mapy == requestEntity.mapy
            ) {
                item.copy(isUserLike = like)
            } else {
                item
            }
        }
        _toggleState.value = updatedList
    }
}