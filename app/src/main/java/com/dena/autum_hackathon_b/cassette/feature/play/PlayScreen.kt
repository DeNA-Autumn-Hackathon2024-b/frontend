package com.dena.autum_hackathon_b.cassette.feature.play

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import com.dena.autum_hackathon_b.cassette.R
import androidx.media3.exoplayer.ExoPlayer
import com.dena.autum_hackathon_b.cassette.feature.play.flipping.FlippingCassetteImage
import com.dena.autum_hackathon_b.cassette.ui.theme.CassetteTheme
import kotlinx.coroutines.isActive
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PlayScreenHost(
    modifier: Modifier = Modifier,
    screenViewModel: PlayViewModel = hiltViewModel(),
    navigateToCreateScreen: () -> Unit
) {
    val screenState = rememberPlayScreenState(screenViewModel)

    LaunchedEffect(screenViewModel) {
        screenViewModel.getCassette()
    }

    DisposableEffect(Unit) {
        onDispose {
            screenState.exoPlayer.release()
        }
    }

    PlayScreen(screenState = screenState, onClickAddButton = navigateToCreateScreen)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayScreen(
    modifier: Modifier = Modifier,
    screenState: PlayScreenState,
    onClickAddButton: () -> Unit
) {
    var isPlaying by remember {mutableStateOf(false)}

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    Box(modifier = Modifier.size(32.dp))
                },
                title = {
                    Text(
                        text = "リラックス",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 24.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                },
                actions = {
                    IconButton(
                        onClick = onClickAddButton
                    ) {
                        Icon(Icons.Default.Add, "Create_screen")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 40.dp)
                .fillMaxSize()
        ) {
            /*
            Text(
                text = "created_at",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )
             */
            // フリップ可能なカセット画像を表示
            FlippingCassetteImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp, bottom = 30.dp),
                screenState = screenState,
            )

            Text(
                text = "time",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
                    .padding(top = 20.dp),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {

                RewindButton(exoPlayer = screenState.exoPlayer)

                IconButton(
                    onClick = {
                        isPlaying = !isPlaying

                        if(isPlaying){
                            //再生用
                            screenState.exoPlayer.play()
                        }
                        else{
                            //停止用
                            screenState.exoPlayer.pause()
                        }
                    }
                ) {
                    if(isPlaying){
                        Icon(
                            painter = painterResource(id = R.drawable.pause),
                            contentDescription = "pause",
                            modifier = Modifier.size(60.dp),
                        )
                    }
                    else {
                        Icon(
                            painter = painterResource(id = R.drawable.play_arrow),
                            contentDescription = "play",
                            modifier = Modifier.size(60.dp),
                        )
                    }
                }

                FastForwardButton(exoPlayer = screenState.exoPlayer)
            }
        }
    }
}

@Composable
fun RewindButton(exoPlayer: ExoPlayer) {
    val coroutineScope = rememberCoroutineScope()
    var rewindJob by remember { mutableStateOf<Job?>(null) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(60.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        Log.d("PlayScreen", "Rewind button pressed")
                        rewindJob = coroutineScope.launch {
                            Log.d("PlayScreen", "Rewind started")
                            while (isActive) {
                                val currentPosition = exoPlayer.currentPosition
                                val newPosition = (currentPosition - 1000).coerceAtLeast(0) // 1秒巻き戻し
                                exoPlayer.seekTo(newPosition)
                                Log.d("PlayScreen", "Rewinding to: $newPosition")
                                delay(500) // 0.5秒ごとに1秒巻き戻し
                            }
                            Log.d("PlayScreen", "Rewind stopped")
                        }
                        tryAwaitRelease()
                        rewindJob?.cancel()
                        Log.d("PlayScreen", "Rewind job cancelled")
                    }
                )
            }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.fast_rewind),
            contentDescription = "rewind",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun FastForwardButton(exoPlayer: ExoPlayer) {
    val coroutineScope = rememberCoroutineScope()
    var fastForwardJob by remember { mutableStateOf<Job?>(null) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(60.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        Log.d("PlayScreen", "FastForward button pressed")
                        fastForwardJob = coroutineScope.launch {
                            Log.d("PlayScreen", "FastForward started")
                            while (isActive) {
                                val currentPosition = exoPlayer.currentPosition
                                val duration = exoPlayer.duration
                                val newPosition = (currentPosition + 1000).coerceAtMost(duration) // 1秒早送り
                                exoPlayer.seekTo(newPosition)
                                Log.d("PlayScreen", "FastForwarding to: $newPosition")
                                delay(500) // 0.5秒ごとに1秒早送り
                            }
                            Log.d("PlayScreen", "FastForward stopped")
                        }
                        tryAwaitRelease()
                        fastForwardJob?.cancel()
                        Log.d("PlayScreen", "FastForward job cancelled")
                    }
                )
            }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.fast_forward),
            contentDescription = "forward",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun PreviewPlayScreen() {
    val screenState = remember {
        mutableStateOf(UiState(cassetteId = null))
    }
    val context = LocalContext.current
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            // 複数の曲をセット
            val mediaItems = listOf(
                MediaItem.fromUri("https://cassette-songs.s3.ap-southeast-2.amazonaws.com/ab814f2c-93b0-41f2-83f9-29dddf5038ee/song1.mp3"),
                MediaItem.fromUri("https://cassette-songs.s3.ap-southeast-2.amazonaws.com/ab814f2c-93b0-41f2-83f9-29dddf5038ee/song2.mp3"),
                MediaItem.fromUri("https://cassette-songs.s3.ap-southeast-2.amazonaws.com/ab814f2c-93b0-41f2-83f9-29dddf5038ee/song3.mp3")
            )
            setMediaItems(mediaItems)
            prepare()
        }
    }
    CassetteTheme {
        PlayScreen(screenState = PlayScreenState(screenState, exoPlayer, context), onClickAddButton = {})
    }
}