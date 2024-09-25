package nacholab.soundboard.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import nacholab.soundboard.domain.MainState
import nacholab.soundboard.domain.AudioClip

@Composable
fun AudioClipItem(
    audioClip: AudioClip,
    isSelected: Boolean,
    playbackState: Int,
    modifier: Modifier = Modifier.fillMaxSize(),
    onClick: () -> Unit
){
    val itemShape = CircleShape

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .then(
                Modifier
                    .aspectRatio(1f)
                    .clickable { onClick.invoke() }
                    .padding(2.dp)
                    .clip(itemShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(6.dp)
                    .border(
                        BorderStroke(
                            1.dp,
                            if (isSelected) MaterialTheme.colorScheme.secondary
                            else MaterialTheme.colorScheme.onPrimary
                        ),
                        itemShape
                    )
                    .padding(8.dp)
                    .border(
                        BorderStroke(
                            1.dp,
                            if (isSelected) MaterialTheme.colorScheme.secondary
                            else MaterialTheme.colorScheme.onPrimary
                        ),
                        itemShape
                    )
                    .padding(8.dp)
            )
    ){
        if (!isSelected) AudioClipItemText(text = audioClip.buildAudioName())
        else when (playbackState){
            MainState.PLAYBACK_STATE_LOADING -> CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxSize(.5f)
            )
            MainState.PLAYBACK_STATE_PLAYING -> {
                PlaybackAnimation()
            }

            else -> AudioClipItemText(text = audioClip.buildAudioName())
        }
    }
}