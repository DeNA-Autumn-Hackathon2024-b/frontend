package com.dena.autum_hackathon_b.cassette.feature.play

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import com.dena.autum_hackathon_b.cassette.feature.create.CreateScreenState
import com.dena.autum_hackathon_b.cassette.feature.create.CreateScreenViewModel
import com.dena.autum_hackathon_b.cassette.feature.create.Song
import com.dena.autum_hackathon_b.cassette.feature.create.rememberCreateScreenState
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
                title = {
                    Text(
                        text = "cassette_name",
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

    IconButton(
        onClick = {},
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    rewindJob = coroutineScope.launch {
                        while (isActive) {
                            val currentPosition = exoPlayer.currentPosition
                            exoPlayer.seekTo((currentPosition - 5000).coerceAtLeast(0))
                            delay(200)
                        }
                    }
                    tryAwaitRelease()
                    rewindJob?.cancel()
                }
            )
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.fast_rewind),
            contentDescription = "rewind",
            modifier = Modifier.size(60.dp),
        )
    }
}

@Composable
fun FastForwardButton(exoPlayer: ExoPlayer) {
    val coroutineScope = rememberCoroutineScope()
    var fastForwardJob by remember { mutableStateOf<Job?>(null) }

    IconButton(
        onClick = {},
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    fastForwardJob = coroutineScope.launch {
                        while (isActive) {
                            val currentPosition = exoPlayer.currentPosition
                            val duration = exoPlayer.duration
                            exoPlayer.seekTo((currentPosition + 5000).coerceAtMost(duration))
                            delay(200)
                        }
                    }
                    tryAwaitRelease()
                    fastForwardJob?.cancel()
                }
            )
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.fast_forward),
            contentDescription = "forward",
            modifier = Modifier.size(60.dp),
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
            setMediaItem(MediaItem.fromUri("https://example.com/song1.m3u8"))
            prepare()
        }
    }
    CassetteTheme {
        PlayScreen(screenState = PlayScreenState(screenState, exoPlayer, context), onClickAddButton = {})
    }
}