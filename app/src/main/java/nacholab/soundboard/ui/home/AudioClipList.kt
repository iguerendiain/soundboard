package nacholab.soundboard.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nacholab.soundboard.BuildConfig
import nacholab.soundboard.domain.MainState
import nacholab.soundboard.domain.AudioClip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioClipList(
    modifier: Modifier = Modifier.fillMaxSize(),
    loadingState: Int,
    audioClips: List<AudioClip>,
    selectedAudioClip: AudioClip?,
    playbackState: Int,
    onClearDBRequested: () -> Unit,
    onRefreshRequested: () -> Unit,
    onAudioClipSelected: (audioClip: AudioClip) -> Unit,
){
    val refreshState = rememberPullToRefreshState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        if (BuildConfig.DEBUG) IconButton(onClick = onClearDBRequested) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
        }
        PullToRefreshBox(
            isRefreshing = loadingState == MainState.LOADING_STATE_LOADING,
            state = refreshState,
            onRefresh = onRefreshRequested
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize()
            ) {
                items(audioClips.size) {
                    audioClips[it].let { audioClip ->
                        AudioClipItem(
                            audioClip = audioClip,
                            isSelected = audioClip == selectedAudioClip,
                            playbackState = playbackState,
                            modifier = Modifier.fillMaxSize(),
                            onClick = { onAudioClipSelected.invoke(audioClip) }
                        )
                    }
                }
            }
        }
    }
}