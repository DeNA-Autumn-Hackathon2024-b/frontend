package com.dena.autum_hackathon_b.cassette.feature.play

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.exoplayer.ExoPlayer

@Stable
class PlayScreenState(private val uiState: State<UiState>, private val exoPlayer: ExoPlayer) {
    val cassetteId: String
        get() = uiState.value.cassetteId ?: ""
}

@Composable
fun rememberPlayScreenState(screenViewModel: PlayViewModel): PlayScreenState {
    val uiState = screenViewModel.uiState
    val context = LocalContext.current
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build()
    }

    return remember(screenViewModel, exoPlayer) {
        PlayScreenState(uiState = uiState, exoPlayer = exoPlayer)
    }
}