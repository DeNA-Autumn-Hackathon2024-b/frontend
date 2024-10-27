package com.dena.autum_hackathon_b.cassette.feature.play

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.media3.exoplayer.ExoPlayer

sealed interface Song {
    data class AddedSong(
        val duration: String,
        val name: String,
        val audioFileUrl: String,
    ) : Song
}

@Stable
class PlayScreenState(private val uiState: State<UiState>, private val exoPlayer: ExoPlayer) {
    val cassetteId: String
        get() = uiState.value.cassetteId ?: ""

    val songs: List<Song>
        get() = listOf(
            // TODO: サーバーから取ってきたカセットの情報（UiState）をここで表示
            Song.AddedSong(
                duration = "0:00",
                name = "ウタ1",
                audioFileUrl = "https://example.com/song1.mp3"
            ),
            Song.AddedSong(
                duration = "0:30",
                name = "ウタ2",
                audioFileUrl = "https://example.com/song2.mp3"
            ),
            Song.AddedSong(
                duration = "1:30",
                name = "ウタ3",
                audioFileUrl = "https://example.com/song3.mp3"
            ),
        )

    val totalDuration: String
        get() = "10:00"

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