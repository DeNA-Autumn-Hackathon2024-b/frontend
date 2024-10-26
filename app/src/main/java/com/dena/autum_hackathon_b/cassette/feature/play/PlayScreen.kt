package com.dena.autum_hackathon_b.cassette.feature.play

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayScreen(modifier: Modifier = Modifier, screenState: PlayScreenState) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
            title = {Text("App_name")},
            modifier = Modifier,
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {Icon(Icons.Default.PlayArrow, "play")},
                    modifier = Modifier,
                    label = {Text("Play")},
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {Icon(Icons.Default.Edit, "create")},
                    modifier = Modifier,
                    label = {Text("Create")},
                    )
            }
        }
    ) { innerPadding ->
        Text(text = screenState.cassetteId, modifier = Modifier.padding(innerPadding))
    }
}

@Preview
@Composable
private fun PreviewPlayScreen() {
    val screenState = remember {
        mutableStateOf(UiState(cassetteId = null))
    }

    CassetteTheme {
        PlayScreen(screenState = PlayScreenState(screenState))
    }
}