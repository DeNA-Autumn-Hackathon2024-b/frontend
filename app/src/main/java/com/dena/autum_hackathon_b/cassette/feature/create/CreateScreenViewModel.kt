package com.dena.autum_hackathon_b.cassette.feature.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class UiState(
    val cassetteTitle: String?
)

@HiltViewModel
class CreateScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState: MutableState<UiState> = mutableStateOf(UiState(cassetteTitle = null))
    val uiState: State<UiState> = _uiState
}