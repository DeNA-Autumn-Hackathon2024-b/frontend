package com.dena.autum_hackathon_b.cassette.feature.play

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Util
import androidx.media3.common.util.Util.getUserAgent
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi

sealed interface Song {
    data class AddedSong(
        val duration: String,
        val name: String,
        val audioFileUrl: String,
    ) : Song
}

@OptIn(UnstableApi::class)
@Stable
class PlayScreenState
    (
    private val uiState: State<UiState>,
    val exoPlayer: ExoPlayer,
    private val context: Context
) {
    val cassetteId: String
        get() = uiState.value.cassetteId ?: ""

    val songs: List<Song>
        get() = listOf(
            // TODO: サーバーから取ってきたカセットの情報（UiState）をここで表示
            Song.AddedSong(
                duration = "0:00",
                name = "ウタ1",
                audioFileUrl = "https://cassette-songs.s3.ap-southeast-2.amazonaws.com/d4fa044f-4457-4c69-85ea-371242c5d20a/d4fa044f[…]d20ad4fa044f-4457-4c69-85ea-371242c5d20a.m3u8"
            ),
            Song.AddedSong(
                duration = "0:30",
                name = "ウタ2",
                audioFileUrl = "https://example.com/song2.m3u8"
            ),
            Song.AddedSong(
                duration = "1:30",
                name = "ウタ3",
                audioFileUrl = "https://example.com/song3.m3u8"
            ),
        )

    val totalDuration: String
        get() = "10:00"

    @OptIn(UnstableApi::class)
    private fun buildMediaSource(url: String): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "cassette-app")
        )
        return HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
    }

    init {
        if (songs.isNotEmpty()) {
            val firstSong = songs[0] as Song.AddedSong
            val mediaSource = buildMediaSource(firstSong.audioFileUrl)
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
        }
    }
}

@Composable
fun rememberPlayScreenState(screenViewModel: PlayViewModel): PlayScreenState {
    val uiState = screenViewModel.uiState
    val context = LocalContext.current
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build()
    }

    return remember(screenViewModel, exoPlayer) {
        PlayScreenState(uiState = uiState, exoPlayer = exoPlayer, context = context)
    }
}