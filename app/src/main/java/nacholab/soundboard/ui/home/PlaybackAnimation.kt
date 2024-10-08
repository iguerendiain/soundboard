package nacholab.soundboard.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import nacholab.soundboard.R

@Composable
fun PlaybackAnimation(modifier: Modifier = Modifier){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.playing_animation))
    LottieAnimation(
        composition = composition,
        modifier = modifier,
        iterations = LottieConstants.IterateForever
    )
}