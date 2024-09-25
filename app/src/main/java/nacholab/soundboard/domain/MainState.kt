package nacholab.soundboard.domain

import nacholab.android.errorhandling.domain.ErrorInfo

data class MainState(
    val audioClips: List<AudioClip> = listOf(),
    val loadingState: Int = LOADING_STATE_IDLE,
    val selectedAudioClip: AudioClip? = null,
    val playbackState: Int = PLAYBACK_STATE_IDLE,
    val audioClipDownloadError: ErrorInfo? = null,
    val playbackError: ErrorInfo? = null
){
    companion object{
        const val LOADING_STATE_LOADING = -1
        const val LOADING_STATE_IDLE = -2

        const val PLAYBACK_STATE_IDLE = -1
        const val PLAYBACK_STATE_LOADING = -2
        const val PLAYBACK_STATE_PLAYING = -3
    }
}