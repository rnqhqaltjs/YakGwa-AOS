package com.prography.yakgwa.ui.createPromise

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CreatePromiseViewModel @Inject constructor(
) : ViewModel() {

    private val _textLengthState20 = MutableStateFlow(0)
    val textLengthState20 = _textLengthState20.asStateFlow()

    private val _textLengthState80 = MutableStateFlow(0)
    val textLengthState80 = _textLengthState80.asStateFlow()

    fun onTextChanged20(text: String) {
        _textLengthState20.value = text.length
    }

    fun onTextChanged80(text: String) {
        _textLengthState80.value = text.length
    }

}