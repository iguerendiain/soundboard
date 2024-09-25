package nacholab.soundboard.domain

import androidx.annotation.OptIn
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nacholab.android.errorhandling.domain.ErrorException
import nacholab.android.errorhandling.domain.ErrorInfo
import nacholab.android.errorhandling.domain.ErrorType
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val mediaPlayer: ExoPlayer
): ViewModel() {

    private val _state = mutableStateOf(MainState())
    val state: State<MainState> = _state

    private var currentlyPlayingURL: String? = null

    init{
        mediaPlayer.addListener(object: Player.Listener{

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_ENDED){
                    mediaPlayer.pause()
                    mediaPlayer.seekTo(0L)
                }

                _state.value = state.value.copy(playbackState = when{
                    playbackState == Player.STATE_READY && mediaPlayer.playWhenReady -> MainState.PLAYBACK_STATE_PLAYING
                    playbackState == Player.STATE_BUFFERING -> MainState.PLAYBACK_STATE_LOADING
                    playbackState == Player.STATE_IDLE -> MainState.PLAYBACK_STATE_IDLE
                    else -> MainState.PLAYBACK_STATE_IDLE
                })
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                _state.value = state.value.copy(
                    playbackState =
                        if (isPlaying) MainState.PLAYBACK_STATE_PLAYING
                        else MainState.PLAYBACK_STATE_IDLE
                )
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)

                val errorType = when (error.errorCode){
                    PlaybackException.ERROR_CODE_INVALID_STATE,
                    PlaybackException.ERROR_CODE_BAD_VALUE,
                    PlaybackException.ERROR_CODE_PERMISSION_DENIED,
                    PlaybackException.ERROR_CODE_NOT_SUPPORTED,
                    PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW,
                    PlaybackException.ERROR_CODE_FAILED_RUNTIME_CHECK,
                    PlaybackException.ERROR_CODE_IO_CLEARTEXT_NOT_PERMITTED ->
                        ErrorType.UNKNOWN

                    PlaybackException.ERROR_CODE_DISCONNECTED,
                    PlaybackException.ERROR_CODE_CONCURRENT_STREAM_LIMIT,
                    PlaybackException.ERROR_CODE_REMOTE_ERROR,
                    PlaybackException.ERROR_CODE_TIMEOUT,
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT,
                    PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS,
                    PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND ->
                        ErrorType.NETWORK

                    PlaybackException.ERROR_CODE_AUTHENTICATION_EXPIRED,
                    PlaybackException.ERROR_CODE_PREMIUM_ACCOUNT_REQUIRED,
                    PlaybackException.ERROR_CODE_PARENTAL_CONTROL_RESTRICTED,
                    PlaybackException.ERROR_CODE_NOT_AVAILABLE_IN_REGION,
                    PlaybackException.ERROR_CODE_SKIP_LIMIT_REACHED,
                    PlaybackException.ERROR_CODE_SETUP_REQUIRED,
                    PlaybackException.ERROR_CODE_IO_NO_PERMISSION,
                    PlaybackException.ERROR_CODE_DRM_UNSPECIFIED,
                    PlaybackException.ERROR_CODE_DRM_SCHEME_UNSUPPORTED,
                    PlaybackException.ERROR_CODE_DRM_PROVISIONING_FAILED,
                    PlaybackException.ERROR_CODE_DRM_CONTENT_ERROR,
                    PlaybackException.ERROR_CODE_DRM_LICENSE_ACQUISITION_FAILED,
                    PlaybackException.ERROR_CODE_DRM_DISALLOWED_OPERATION,
                    PlaybackException.ERROR_CODE_DRM_SYSTEM_ERROR,
                    PlaybackException.ERROR_CODE_DRM_DEVICE_REVOKED,
                    PlaybackException.ERROR_CODE_DRM_LICENSE_EXPIRED,
                    PlaybackException.ERROR_CODE_END_OF_PLAYLIST,
                    PlaybackException.ERROR_CODE_CONTENT_ALREADY_PLAYING,
                    PlaybackException.ERROR_CODE_UNSPECIFIED ->
                        ErrorType.UNKNOWN

                    PlaybackException.ERROR_CODE_IO_UNSPECIFIED,
                    PlaybackException.ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE,
                    PlaybackException.ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE,
                    PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED,
                    PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED,
                    PlaybackException.ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED,
                    PlaybackException.ERROR_CODE_PARSING_MANIFEST_UNSUPPORTED,
                    PlaybackException.ERROR_CODE_DECODER_INIT_FAILED,
                    PlaybackException.ERROR_CODE_DECODER_QUERY_FAILED,
                    PlaybackException.ERROR_CODE_DECODING_FAILED,
                    PlaybackException.ERROR_CODE_DECODING_FORMAT_EXCEEDS_CAPABILITIES,
                    PlaybackException.ERROR_CODE_DECODING_FORMAT_UNSUPPORTED,
                    PlaybackException.ERROR_CODE_AUDIO_TRACK_INIT_FAILED,
                    PlaybackException.ERROR_CODE_AUDIO_TRACK_WRITE_FAILED,
                    PlaybackException.ERROR_CODE_AUDIO_TRACK_OFFLOAD_WRITE_FAILED,
                    PlaybackException.ERROR_CODE_AUDIO_TRACK_OFFLOAD_INIT_FAILED ->
                        ErrorType.PARSE

                    else -> ErrorType.UNKNOWN
                }

                _state.value = state.value.copy(
                    playbackError = ErrorInfo(
                        type = errorType,
                        errorBody = "[${error.errorCodeName}]: ${error.message}",
                        statusCode = error.errorCode,
                        exception = error
                    )
                )
            }
        })
    }

    fun clearPlaybackError(){
        _state.value = state.value.copy(playbackError = null)
    }

    fun selectAudioClip(audioClip: AudioClip){
        toggleAudioClipPlayback(audioClip)
        _state.value = state.value.copy(selectedAudioClip = audioClip)
    }

    fun clearDB(){
        _state.value = state.value.copy(loadingState = MainState.LOADING_STATE_LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            stopAudioClipPlayback()
            mainRepository.clearLocalAudioClips()
            withContext(Dispatchers.Main){
                _state.value = state.value.copy(
                    loadingState = MainState.LOADING_STATE_IDLE,
                    audioClips = listOf(),
                    selectedAudioClip = null,
                    playbackState = MainState.PLAYBACK_STATE_IDLE
                )
            }
        }
    }

    fun refreshAudioclips(){
        _state.value = state.value.copy(
            loadingState = MainState.LOADING_STATE_LOADING,
        )
        viewModelScope.launch(Dispatchers.IO) {
            val downloadAudioClipsResult = mainRepository.downloadAudioClips()

            if (downloadAudioClipsResult.isSuccess) {
                downloadAudioClipsResult.getOrNull()?.let { audioClips ->
                    stopAudioClipPlayback()
                    mainRepository.clearLocalAudioClips()
                    mainRepository.storeAudioClips(audioClips)
                    val localAudioClips = mainRepository.getLocalAudioClips()

                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(
                            loadingState = MainState.LOADING_STATE_IDLE,
                            audioClips = localAudioClips.getOrNull()?: listOf()
                        )
                    }
                }
            }else{
                if (state.value.audioClips.isEmpty()) {
                    val apiErrorInfo = (downloadAudioClipsResult.exceptionOrNull() as ErrorException?)
                        ?.errorInfo
                        ?: ErrorInfo(type = ErrorType.UNKNOWN)

                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(
                            loadingState = MainState.LOADING_STATE_IDLE,
                            audioClipDownloadError = apiErrorInfo
                        )
                    }
                }
            }
        }
    }

    fun clearAudioClipDownloadError(){
        _state.value = state.value.copy(audioClipDownloadError = null)
    }

    @OptIn(UnstableApi::class)
    private fun toggleAudioClipPlayback(audioClip: AudioClip){
        viewModelScope.launch(Dispatchers.Main){
            val url = audioClip.buildAudioURL()

            if (url == currentlyPlayingURL){
                if (mediaPlayer.isPlaying) mediaPlayer.pause()
                else mediaPlayer.play()
            }else{
                currentlyPlayingURL = url
                mediaPlayer.stop()
                mediaPlayer.setMediaItem(MediaItem.fromUri(url))
                mediaPlayer.prepare()
                mediaPlayer.play()
            }
        }
    }

    fun stopAudioClipPlayback(){
        viewModelScope.launch(Dispatchers.Main) {
            mediaPlayer.stop()
            currentlyPlayingURL = null
        }
    }
}