package com.dena.autum_hackathon_b.cassette.feature.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.extractor.metadata.scte35.SpliceCommand
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.dena.autum_hackathon_b.cassette.R
import com.dena.autum_hackathon_b.cassette.entity.CachedAudioFile
import com.dena.autum_hackathon_b.cassette.ui.theme.CassetteTheme
import okhttp3.Cache

@Composable
fun CreateScreenHost(
    modifier: Modifier = Modifier,
    navController: NavController,
    screenViewModel: CreateScreenViewModel = hiltViewModel(),
    navigateToAddSongDialog: () -> Unit,
    navigateBack: () -> Unit,
) {
    val screenState = rememberCreateScreenState(screenViewModel)

    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.run {
            if (savedStateHandle.contains("cachedAudioFile") && savedStateHandle.contains("songText")) { // 結果があるか確認
                // 結果取得
                val cachedAudioFile: CachedAudioFile? = savedStateHandle["cachedAudioFile"]
                val songText: String? = savedStateHandle["songText"]
                // 結果を使った処理
                if (cachedAudioFile != null && songText != null) {
                    screenViewModel.registerSong(cachedAudioFile, songText)
                }
                // 結果を削除
                savedStateHandle.remove<CachedAudioFile>("result")
            }
        }
    }

    CreateScreen(
        modifier = modifier,
        screenState = screenState,
        onClickAddSong = navigateToAddSongDialog,
        onClickBack = navigateBack,
        onClickUploadButton = {
            screenViewModel.uploadSongs()
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    screenState: CreateScreenState,
    onClickAddSong: () -> Unit,
    onClickBack: () -> Unit,
    onClickUploadButton: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "新しいカセットを作成")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back_24px),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    Box(modifier = Modifier.size(32.dp))
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onClickUploadButton) {
                Icon(
                    painter = painterResource(R.drawable.ic_send_gray_24dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentDescription = ""
                )
            }
        },
    ) { innerPadding ->
        val layoutDirection = LocalLayoutDirection.current
        LazyColumn(
            modifier = Modifier.consumeWindowInsets(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                start = innerPadding.calculateStartPadding(layoutDirection) + 16.dp,
                top = innerPadding.calculateTopPadding() + 16.dp,
                end = innerPadding.calculateEndPadding(layoutDirection) + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 96.dp
            )
        ) {
            item {
                Column {
                    OutlinedTextField(
                        value = screenState.cassetteName,
                        onValueChange = screenState::updateCassetteName,
                        placeholder = {
                            Text(text = "カセットの名前")
                        },
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            itemsIndexed(screenState.songs) { index, song ->
                Box(
                    modifier = Modifier
                        .clip(
                            shape = when (index) {
                                0 -> RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                screenState.songs.size - 1 -> RoundedCornerShape(
                                    bottomStart = 8.dp,
                                    bottomEnd = 8.dp
                                )

                                else -> RectangleShape
                            }
                        )
                        .background(color = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    when (song) {
                        Song.AddSong -> AddSongRow(onClick = onClickAddSong)
                        is Song.AddedSong -> AddedSongRow(song = song)
                    }

                    if (index != 0) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        if (screenState.uiState.value.loading) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        val url = screenState.uiState.value.url
        if (url != null) {
            Dialog(
                onDismissRequest = onClickBack,
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(16.dp),
                ) {

                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "アップロード完了しました！以下のURLを共有してください！")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "$url")

                        Spacer(modifier = Modifier.weight(1f))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.weight(1f))
                            Button(onClick = onClickBack) { Text(text = "ok") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddedSongRow(modifier: Modifier = Modifier, song: Song.AddedSong) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = song.duration)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = song.name)
    }
}

@Composable
fun AddSongRow(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(12.dp),
            painter = painterResource(R.drawable.ic_add_24px),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "新しいオーディオを追加")
    }
}

@Preview
@Composable
private fun PreviewCreateScreen() {
    val screenState = remember {
        mutableStateOf(UiState(cassetteTitle = null))
    }

    CassetteTheme {
        CreateScreen(
            screenState = CreateScreenState(screenState),
            onClickAddSong = {},
            onClickBack = {},
            onClickUploadButton = {}
        )
    }
}
