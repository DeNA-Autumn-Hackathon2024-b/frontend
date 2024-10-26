package com.dena.autum_hackathon_b.cassette.feature.play.flipping

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dena.autum_hackathon_b.cassette.R

@Composable
fun FlippingCassetteImage(
    modifier: Modifier = Modifier
){
        var isFlipped by remember {mutableStateOf(false) }

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
            }
            .clickable {
                isFlipped = !isFlipped
            }
    ){
        if(isBackVisible){
            Image(
                painter = painterResource(id = R.drawable.cassette_image),
                contentDescription = "Cassette back",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 30.dp)
            )
        }
        else {
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