package com.dena.autum_hackathon_b.cassette.feature.create.addsong

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dena.autum_hackathon_b.cassette.data.cassette.CassetteRepository
import com.dena.autum_hackathon_b.cassette.entity.CachedAudioFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class UiState(
    val cachedAudioFile: CachedAudioFile? = null
)

@HiltViewModel
class AddSongDialogScreenViewModel @Inject constructor(
    private val cassetteRepository: CassetteRepository
) : ViewModel() {
    private val _uiState: MutableState<UiState> = mutableStateOf(UiState())
    val uiState: State<UiState> = _uiState

    fun cacheAudioFile(audioUri: Uri) {
        viewModelScope.launch {
            val cachedAudioFile = withContext(Dispatchers.IO) {
                cassetteRepository.cacheAudio(audioUri)
            }

            _uiState.value = uiState.value.copy(cachedAudioFile = cachedAudioFile)
        }
    }
}
