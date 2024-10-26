package com.dena.autum_hackathon_b.cassette.feature.create

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dena.autum_hackathon_b.cassette.R
import com.dena.autum_hackathon_b.cassette.ui.theme.CassetteTheme

@Composable
fun CreateScreenHost(
    modifier: Modifier = Modifier,
    screenViewModel: CreateScreenViewModel = hiltViewModel()
) {
    val screenState = rememberCreateScreenState(screenViewModel)

    LaunchedEffect(screenViewModel) {
    }

    CreateScreen(screenState = screenState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(modifier: Modifier = Modifier, screenState: CreateScreenState) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "Create new cassette")
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.ic_send_gray_24dp),
                    tint = Color.Black,
                    contentDescription = ""
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Column {
                    Text(text = "カセットの名前")
                    OutlinedTextField(
                        value = screenState.cassetteName,
                        onValueChange = screenState::updateCassetteName,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(screenState.songs) { song ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            brush = SolidColor(value = Color.Black),
                            shape = RectangleShape
                        )
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = song.duration)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = song.name)
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = screenState.totalDuration)
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedTextField(
                        value = screenState.newSongName,
                        onValueChange = screenState::updateNewSongName
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = {}) {
                        Text(text = "+")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCreateScreen() {
    val screenState = remember {
        mutableStateOf(UiState(cassetteTitle = null))
    }

    CassetteTheme {
        CreateScreen(screenState = CreateScreenState(screenState))
    }
}
