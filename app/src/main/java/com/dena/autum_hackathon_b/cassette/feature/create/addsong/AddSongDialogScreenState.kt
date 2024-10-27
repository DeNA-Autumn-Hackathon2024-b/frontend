package com.dena.autum_hackathon_b.cassette.feature.create.addsong

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dena.autum_hackathon_b.cassette.entity.CachedAudioFile

@Stable
class AddSongDialogScreenState(private val uiState: State<UiState>) {
    sealed interface ScreenState {
        data object Init : ScreenState
        data class Loaded(val cachedAudioFile: CachedAudioFile) : ScreenState
    }

    val state: ScreenState
        get() {
            val cachedAudioFile = uiState.value.cachedAudioFile
            return when {
                cachedAudioFile != null -> {
                    ScreenState.Loaded(cachedAudioFile)
                }

                else -> ScreenState.Init
            }
        }

    var songText: String by mutableStateOf("")
        private set

    fun updateSongText(text: String) {
        songText = text
    }
}

@Composable
fun rememberAddSongDialogPageState(screenViewModel: AddSongDialogScreenViewModel): AddSongDialogScreenState {
    val uiState = screenViewModel.uiState

    return remember(screenViewModel) {
        AddSongDialogScreenState(uiState = uiState)
    }
}