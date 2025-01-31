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
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.source.ProgressiveMediaSource

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
                name = "風のささやき",
                audioFileUrl = "https://cassette-songs.s3.ap-southeast-2.amazonaws.com/ab814f2c-93b0-41f2-83f9-29dddf5038ee/original.mp3"
            ),
            Song.AddedSong(
                duration = "0:30",
                name = "希望の光",
                audioFileUrl = "https://cassette-songs.s3.ap-southeast-2.amazonaws.com/4ead1714-a222-491e-bd73-c771481f7585/original.mp3"
            ),
            Song.AddedSong(
                duration = "1:30",
                name = "星降る夜のメロディ",
                audioFileUrl = "https://cassette-songs.s3.ap-southeast-2.amazonaws.com/9493b5d5-80a2-43ca-8755-1614764e6bd1/original.mp3"
            ),
        )

    val totalDuration: String
        get() = "10:00"

    //MP3
    private fun buildMediaSource(url: String): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "cassette-app")
        )
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
    }

   //HLS
    /*
    @OptIn(UnstableApi::class)
    private fun buildMediaSource(url: String): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "cassette-app")
        )
        return HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
    }
     */

    init {
        if (songs.isNotEmpty()) {
            val mediaItems = songs.filterIsInstance<Song.AddedSong>().map { song ->
                MediaItem.fromUri(Uri.parse(song.audioFileUrl))
            }
            exoPlayer.setMediaItems(mediaItems)
            exoPlayer.prepare()

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_READY -> {
                            println("ExoPlayer is ready and seekable: ${exoPlayer.isCurrentWindowSeekable}")
                        }
                        Player.STATE_BUFFERING -> {
                            println("ExoPlayer is buffering")
                        }
                        Player.STATE_ENDED -> {
                            println("Playback ended")
                        }
                    }
                }
            })
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