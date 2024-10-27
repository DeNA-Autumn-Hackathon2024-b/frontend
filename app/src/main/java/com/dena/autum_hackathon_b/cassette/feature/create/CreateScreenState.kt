package com.dena.autum_hackathon_b.cassette.feature.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

sealed interface Song {
    data class AddedSong(
        val duration: String,
        val name: String,
    ) : Song

    data object AddSong : Song
}

@Stable
class CreateScreenState(private val uiState: State<UiState>) {
    var cassetteName: String by mutableStateOf("")
        private set

    fun updateCassetteName(name: String) {
        cassetteName = name
    }

    val songs: List<Song>
        get() = uiState.value.songList.map { cassetteSong ->
            Song.AddedSong(name = cassetteSong.name, duration = "00:00")
        } + listOf(Song.AddSong)

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