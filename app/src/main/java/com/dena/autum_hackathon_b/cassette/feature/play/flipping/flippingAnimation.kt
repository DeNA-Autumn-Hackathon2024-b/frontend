package com.dena.autum_hackathon_b.cassette.feature.play.flipping

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dena.autum_hackathon_b.cassette.R
import com.dena.autum_hackathon_b.cassette.feature.play.PlayScreenState
import com.dena.autum_hackathon_b.cassette.feature.play.Song

@Composable
fun FlippingCassetteImage(
    modifier: Modifier = Modifier,
    screenState: PlayScreenState
) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        )
    )

    val isBackVisible = rotation > 90f

    Box(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
                scaleX = if (rotation in 90f..270f) -1f else 1f
            }
            .clickable {
                isFlipped = !isFlipped
            },
        contentAlignment = Alignment.Center
    ) {
        if (isBackVisible) {

            val layoutDirection = LocalLayoutDirection.current

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                /*
                contentPadding = PaddingValues(
                    start = innerPadding.calculateStartPadding(layoutDirection) + 16.dp,
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    end = innerPadding.calculateEndPadding(layoutDirection) + 16.dp,
                    bottom = innerPadding.calculateBottomPadding() + 96.dp
                )
                 */
            ) {
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
        } else {
            Image(
                painter = painterResource(id = R.drawable.cassette_image),
                contentDescription = "Cassette front",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 30.dp)
            )
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