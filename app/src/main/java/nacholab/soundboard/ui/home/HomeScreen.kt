package nacholab.soundboard.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import nacholab.android.errorhandling.ui.ErrorInfoDialog
import nacholab.soundboard.R

@Composable
fun HomeScreen(mainViewModel: MainViewModel){
    val mainState = mainViewModel.state.value

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (mainState.audioClips.isEmpty()) EmptyDB(
            loadingState = mainState.loadingState,
            modifier = Modifier.fillMaxSize(),
            onRefreshRequested = { mainViewModel.refreshAudioclips() }
        ) else AudioClipList(
            loadingState = mainState.loadingState,
            audioClips = mainState.audioClips,
            selectedAudioClip = mainState.selectedAudioClip,
            playbackState = mainState.playbackState,
            onClearDBRequested = { mainViewModel.clearDB() },
            onRefreshRequested = { mainViewModel.refreshAudioclips() },
            onAudioClipSelected = { mainViewModel.selectAudioClip(it) }
        )

        if (mainState.audioClipDownloadError!=null) ErrorInfoDialog(
            title = stringResource(id = R.string.home_error_audioclips_download),
            errorInfo = mainState.audioClipDownloadError,
            onRetry = {
                mainViewModel.clearAudioClipDownloadError()
                mainViewModel.refreshAudioclips()
            },
            onDismiss = { mainViewModel.clearAudioClipDownloadError() }
        )

        if (mainState.playbackError!=null) ErrorInfoDialog(
            title = stringResource(id = R.string.home_error_audio_playback),
            errorInfo = mainState.playbackError,
            onRetry = null,
            onDismiss = {
                mainViewModel.stopAudioClipPlayback()
                mainViewModel.clearPlaybackError()
            }
        )
    }
}