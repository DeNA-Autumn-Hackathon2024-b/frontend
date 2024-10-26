package com.dena.autum_hackathon_b.cassette.feature.play

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Stable
class PlayScreenState(private val uiState: State<UiState>) {
    val cassetteId: String
        get() = uiState.value.cassetteId ?: ""
}

@Composable
fun rememberPlayScreenState(screenViewModel: PlayViewModel): PlayScreenState {
    val uiState = screenViewModel.uiState

    return remember(screenViewModel) {
        PlayScreenState(uiState = uiState)
    }
}