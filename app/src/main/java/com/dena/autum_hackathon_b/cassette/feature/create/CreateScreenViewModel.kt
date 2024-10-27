package com.dena.autum_hackathon_b.cassette.feature.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dena.autum_hackathon_b.cassette.data.cassette.CassetteRepository
import com.dena.autum_hackathon_b.cassette.entity.CachedAudioFile
import com.dena.autum_hackathon_b.cassette.entity.Cassette
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

data class UiState(
    val cassetteTitle: String?,
    val songList: List<Cassette.Song> = emptyList(),
    val loading: Boolean = false,
    val url: String? = null
)

@HiltViewModel
class CreateScreenViewModel @Inject constructor(
    private val cassetteRepository: CassetteRepository
) : ViewModel() {
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

    fun uploadSongs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            val url = withContext(Dispatchers.IO) {
                val song = uiState.value.songList.first()
                cassetteRepository.uploadAudio(
                    song.cachedAudioFile.audioFile,
                    song.cachedAudioFile.mimeType,
                    song.name,
                    100,
                    0
                )
            }

            _uiState.value = _uiState.value.copy(url = url, loading = false)
        }
    }
}