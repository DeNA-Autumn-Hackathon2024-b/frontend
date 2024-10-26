package com.dena.autum_hackathon_b.cassette.feature.play

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dena.autum_hackathon_b.cassette.data.cassette.CassetteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class UiState(
    val cassetteId: String?
)

@HiltViewModel
class PlayViewModel @Inject constructor(
    private val cassetteRepository: CassetteRepository
) : ViewModel() {
    private val _uiState: MutableState<UiState> = mutableStateOf(UiState(cassetteId = null))
    val uiState: State<UiState> = _uiState

    fun getCassette() {
        viewModelScope.launch {
            val cassette = withContext(Dispatchers.IO) {
                cassetteRepository.getCassette("123")
            }

            _uiState.value = _uiState.value.copy(cassetteId = cassette.id)
        }
    }
}