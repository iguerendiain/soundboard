package nacholab.soundboard.domain

import nacholab.soundboard.model.AudioClip


interface MainRepository {

    suspend fun downloadAudioClips(): Result<List<AudioClip>>

    suspend fun storeAudioClips(audioClips: List<AudioClip>): Result<Unit>

    suspend fun getLocalAudioClips(): Result<List<AudioClip>>

    suspend fun clearLocalAudioClips(): Result<Unit>

}