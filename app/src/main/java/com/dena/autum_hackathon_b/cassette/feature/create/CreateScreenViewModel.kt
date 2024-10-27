package com.dena.autum_hackathon_b.cassette.feature.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dena.autum_hackathon_b.cassette.entity.CachedAudioFile
import com.dena.autum_hackathon_b.cassette.entity.Cassette
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

data class UiState(
    val cassetteTitle: String?,
    val songList: List<Cassette.Song> = emptyList()
)

@HiltViewModel
class CreateScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState: MutableState<UiState> = mutableStateOf(UiState(cassetteTitle = null))
    val uiState: State<UiState> = _uiState

    fun registerSong(cachedAudioFile: CachedAudioFile, songText: String) {
        Timber.d("audiofile name: ${cachedAudioFile.fileName}, songname: $songText ")
        _uiState.value = _uiState.value.copy(
            songList = uiState.value.songList + listOf(
                Cassette.Song(
                    name = songText,
                    cachedAudioFile = cachedAudioFile
                )
            )
        )
    }
}