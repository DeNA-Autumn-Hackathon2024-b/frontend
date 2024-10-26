package com.dena.autum_hackathon_b.cassette.feature.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

data class Song(
    val duration: String,
    val name:String,
    val audioFileUrl: String,
)

@Stable
class CreateScreenState(private val uiState: State<UiState>) {
    var cassetteName: String by mutableStateOf("")
        private set

    fun updateCassetteName(name: String) {
        cassetteName = name
    }

    val songs: List<Song>
        get() = listOf(
            Song(duration = "0:00", name = "ウタ1", audioFileUrl = "https://example.com/song1.mp3"),
            Song(duration = "0:30", name = "ウタ2", audioFileUrl = "https://example.com/song2.mp3"),
            Song(duration = "1:30", name = "ウタ3", audioFileUrl = "https://example.com/song3.mp3"),
        )

    val totalDuration: String
        get() = "10:00"

    var newSongName: String by mutableStateOf("")
        private set

    fun updateNewSongName(name: String) {
        newSongName = name
    }
}

@Composable
fun rememberCreateScreenState(screenViewModel: CreateScreenViewModel): CreateScreenState {
    return remember(screenViewModel) {
        CreateScreenState(uiState = screenViewModel.uiState)
    }
}