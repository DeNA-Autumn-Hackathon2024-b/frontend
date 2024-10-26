package com.dena.autum_hackathon_b.cassette.feature.play

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dena.autum_hackathon_b.cassette.R
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
                title = { Text(
                    text = "cassette_name",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                ) },
                actions = { IconButton(
                    onClick = {}
                ) {
                    Icon(Icons.Default.Add, "Create_screen")
                }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
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
            Image(
                painter = painterResource(id = R.drawable.cassette_image),
                contentDescription = "Figure Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp, bottom = 30.dp)
            )

            Text(
                text = "time",
                modifier = Modifier.fillMaxWidth()
                    .weight(0.2f)
                    .padding(top = 20.dp),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .weight(0.3f),
                horizontalArrangement = Arrangement.SpaceAround,
            ){
                IconButton(
                    onClick = { /* TODO: 巻き戻しボタンの挙動*/ }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.fast_rewind),
                        contentDescription = "rewind",
                        modifier = Modifier.size(60.dp),
                    )
                }
                IconButton(
                    onClick = { /* TODO: 再生ボタンの挙動*/ }
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        "play",
                        modifier = Modifier.size(60.dp),
                    )
                }
                IconButton(
                    onClick = { /* TODO: 早送りボタンの挙動*/ }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.fast_forward),
                        contentDescription = "forward",
                        modifier = Modifier.size(60.dp),
                    )
                }
            }
        }
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