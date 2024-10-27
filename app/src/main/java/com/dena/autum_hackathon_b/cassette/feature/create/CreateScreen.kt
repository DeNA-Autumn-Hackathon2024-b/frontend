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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.dena.autum_hackathon_b.cassette.R
import com.dena.autum_hackathon_b.cassette.ui.theme.CassetteTheme

@Composable
fun CreateScreenHost(
    modifier: Modifier = Modifier,
    screenViewModel: CreateScreenViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val screenState = rememberCreateScreenState(screenViewModel)

    LaunchedEffect(screenViewModel) {
    }

    CreateScreen(modifier = modifier, screenState = screenState, onClickBack = navigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    screenState: CreateScreenState,
    onClickBack: () -> Unit
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
            FloatingActionButton(onClick = {}) {
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
                        Song.AddSong -> AddSongRow(onClick = {})
                        is Song.AddedSong -> AddedSongRow(
                            index = index,
                            size = screenState.songs.size,
                            song = song
                        )
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
    }
}

@Composable
fun AddedSongRow(modifier: Modifier = Modifier, size: Int, index: Int, song: Song.AddedSong) {
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
        CreateScreen(screenState = CreateScreenState(screenState), onClickBack = {})
    }
}
