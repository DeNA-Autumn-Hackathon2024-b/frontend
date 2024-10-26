package com.dena.autum_hackathon_b.cassette.feature.play

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.exoplayer.ExoPlayer
import com.dena.autum_hackathon_b.cassette.ui.theme.CassetteTheme

@Composable
fun PlayScreenHost(
    modifier: Modifier = Modifier,
    screenViewModel: PlayViewModel = hiltViewModel()
) {
    val screenState = rememberPlayScreenState(screenViewModel)

    LaunchedEffect(screenViewModel) {
        screenViewModel.getCassette()
    }

    PlayScreen(screenState = screenState)
}

@Composable
fun PlayScreen(modifier: Modifier = Modifier, screenState: PlayScreenState) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(text = screenState.cassetteId, modifier = Modifier.padding(innerPadding))
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
        ExoPlayer.Builder(context).build()
    }

    CassetteTheme {
        PlayScreen(screenState = PlayScreenState(screenState, exoPlayer))
    }
}