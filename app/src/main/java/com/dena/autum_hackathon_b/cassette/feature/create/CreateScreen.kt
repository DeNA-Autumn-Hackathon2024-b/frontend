package com.dena.autum_hackathon_b.cassette.feature.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dena.autum_hackathon_b.cassette.R
import com.dena.autum_hackathon_b.cassette.ui.theme.CassetteTheme
import com.dena.autum_hackathon_b.cassette.ui.widget.bottomBorder
import com.dena.autum_hackathon_b.cassette.ui.widget.sideBorder
import com.dena.autum_hackathon_b.cassette.ui.widget.topBorder

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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "新しいカセットを作成")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
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
                when (song) {
                    Song.AddSong -> AddSongRow(onClick = {})
                    is Song.AddedSong -> AddedSongRow(
                        index = index,
                        size = screenState.songs.size,
                        song = song
                    )
                }

                if (index != screenState.songs.size - 1) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun AddedSongRow(modifier: Modifier = Modifier, size: Int, index: Int, song: Song.AddedSong) {
    val borderColor = MaterialTheme.colorScheme.secondary
    val border = remember(index) {
        when (index) {
            0 -> {
                Modifier.topBorder(
                    strokeWidth = 2.dp,
                    color = borderColor,
                    cornerRadiusDp = 8.dp
                )
            }

            size - 1 -> {
                Modifier.bottomBorder(
                    strokeWidth = 2.dp,
                    color = borderColor,
                    cornerRadiusDp = 8.dp
                )
            }

            else -> {
                Modifier.sideBorder(
                    strokeWidth = 2.dp,
                    color = borderColor,
                    cornerRadiusDp = 0.dp
                )
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(border)
            .clip(
                shape = when (index) {
                    0 -> RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    size - 1 -> RoundedCornerShape(
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp
                    )

                    else -> RectangleShape
                }
            )
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
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
    val borderColor = MaterialTheme.colorScheme.secondary
    val border =
        Modifier.bottomBorder(
            strokeWidth = 2.dp,
            color = borderColor,
            cornerRadiusDp = 8.dp
        )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(border)
            .clip(
                shape = RoundedCornerShape(
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
            )
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
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
        CreateScreen(screenState = CreateScreenState(screenState))
    }
}
