package com.dena.autum_hackathon_b.cassette.feature.create.addsong

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dena.autum_hackathon_b.cassette.R
import com.dena.autum_hackathon_b.cassette.entity.CachedAudioFile
import timber.log.Timber

@Composable
fun AddSongDialogScreenHost(
    modifier: Modifier = Modifier,
    pageViewModel: AddSongDialogScreenViewModel = hiltViewModel(),
    onSuccess: (CachedAudioFile, String) -> Unit,
    onCancel: () -> Unit
) {
    val screenState = rememberAddSongDialogPageState(pageViewModel)

    AddSongDialogScreen(
        modifier = modifier,
        screenState = screenState,
        onAudioSelected = { audioUri ->
            pageViewModel.cacheAudioFile(audioUri)
        },
        onClickBack = onCancel,
        onClickCheck = {
            val state = screenState.state
            if (state is AddSongDialogScreenState.ScreenState.Loaded) {
                onSuccess(state.cachedAudioFile, screenState.songText)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSongDialogScreen(
    modifier: Modifier = Modifier,
    screenState: AddSongDialogScreenState,
    onAudioSelected: (Uri) -> Unit,
    onClickBack: () -> Unit,
    onClickCheck: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val audioUri: Uri = data.data!!
                onAudioSelected(audioUri)
                Timber.d("get uri is $audioUri")
            }
        }
    }
    val pickAudioIntent = remember {
        Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "audio/*"
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "オーディオを追加")
                },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_24px),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (screenState.state is AddSongDialogScreenState.ScreenState.Loaded) {
                        IconButton(onClick = onClickCheck) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check_24px),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val state = screenState.state) {
            AddSongDialogScreenState.ScreenState.Init -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            launcher.launch(pickAudioIntent)
                        }
                    ) {
                        Text(text = "オーディオファイルを選択")
                    }
                }
            }

            is AddSongDialogScreenState.ScreenState.Loaded -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.cachedAudioFile.fileName)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = screenState.songText,
                        onValueChange = screenState::updateSongText,
                        placeholder = {
                            Text(text = "セトリに表示する曲名")
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAddSongDialogScreen() {
    val uiState = remember {
        mutableStateOf(UiState())
    }
    MaterialTheme {
        AddSongDialogScreen(
            screenState = AddSongDialogScreenState(uiState),
            onAudioSelected = {},
            onClickBack = {},
            onClickCheck = {}
        )
    }
}