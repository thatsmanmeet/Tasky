package com.thatsmanmeet.taskyapp.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.thatsmanmeet.taskyapp.R


@Composable
fun TaskCompleteAnimations(
    isLottiePlaying: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.side))
    val progressAnimation by animateLottieCompositionAsState(composition = lottieComposition, isPlaying = isLottiePlaying.value, speed = 1.4f)
    LottieAnimation(
        composition = lottieComposition,
        progress = {
            progressAnimation
        },
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

}