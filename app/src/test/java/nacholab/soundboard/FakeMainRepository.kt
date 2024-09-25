package nacholab.soundboard

import nacholab.soundboard.domain.AudioClip
import nacholab.soundboard.domain.MainRepository

class FakeMainRepository(private val fakeAudioClipList: List<AudioClip>): MainRepository {

    override suspend fun downloadAudioClips(): Result<List<AudioClip>> {
        return Result.success(fakeAudioClipList)
    }

    override suspend fun storeAudioClips(audioClips: List<AudioClip>): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getLocalAudioClips(): Result<List<AudioClip>> {
        return Result.success(fakeAudioClipList)
    }

    override suspend fun clearLocalAudioClips(): Result<Unit> {
        return Result.success(Unit)
    }
}