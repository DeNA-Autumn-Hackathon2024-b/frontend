package com.dena.autum_hackathon_b.cassette.feature.play

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dena.autum_hackathon_b.cassette.R
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
                    onClick = {/*TODO:ここでPlay_screenへのナビゲーション行う*/},
                    icon = { Icon(
                        painter = painterResource(id = R.drawable.music_note),
                        contentDescription = "play"
                        ) },
                    modifier = Modifier,
                    label = {Text("Play")},
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {/*TODO:ここでCreate_screenへのナビゲーション行う*/},
                    icon = {Icon(Icons.Default.Edit, "create")},
                    modifier = Modifier,
                    label = {Text("Create")},
                    )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {/* TODO: 後でセトリの表示 */},
                modifier = Modifier,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.description),
                    contentDescription = "description",
                    modifier = Modifier.size(60.dp),
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .padding(top = 40.dp)
        ) {
            Text(
                text = "cassette_name",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "created_at",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )
            Image(
                painter = painterResource(id = R.drawable.cassette_image),
                contentDescription = "Figure Image",
                modifier = Modifier
                    .fillMaxWidth() // 必要に応じてサイズを指定
                    .padding(top = 16.dp, bottom = 30.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ){
                Icon(
                    painter = painterResource(id = R.drawable.fast_rewind),
                    contentDescription = "rewind",
                    modifier = Modifier.size(60.dp),
                )
                Icon(
                    Icons.Default.PlayArrow,
                    "play",
                    modifier = Modifier.size(60.dp),
                    )
                Icon(
                    painter = painterResource(id = R.drawable.fast_forward),
                    contentDescription = "forward",
                    modifier = Modifier.size(60.dp),
                )
            }
            Text(
                text = "time",
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 20.dp),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )
        }
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