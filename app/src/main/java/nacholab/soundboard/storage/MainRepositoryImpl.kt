package nacholab.soundboard.storage

import nacholab.soundboard.domain.ErrorException
import nacholab.soundboard.domain.ErrorInfo
import nacholab.soundboard.api.MainAPI
import nacholab.soundboard.domain.MainRepository
import nacholab.soundboard.model.AudioClip
import java.io.IOException
import java.lang.RuntimeException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val db: MainDB,
    private val api: MainAPI
): MainRepository {

    override suspend fun downloadAudioClips(): Result<List<AudioClip>> {
        return try {
            val audioClipsResponse = api.downloadAudioClips()

            if (audioClipsResponse.isSuccessful) {
                val audioClips = audioClipsResponse.body()
                if (audioClips != null) Result.success(audioClips)
                else Result.failure(
                    ErrorException(ErrorInfo(type = ErrorInfo.EMPTY))
                )
            } else
                Result.failure(
                    ErrorException(
                        ErrorInfo(
                            type = ErrorInfo.UNKNOWN,
                            errorBody = audioClipsResponse.errorBody()?.string(),
                            statusCode = audioClipsResponse.code(),
                            exception = null
                        )
                    )
                )
        }catch (e: Exception){
            val errorType = when (e) {
                is IOException -> ErrorInfo.NETWORK
                is RuntimeException -> ErrorInfo.PARSE
                else -> ErrorInfo.UNKNOWN
            }

            Result.failure(ErrorException(ErrorInfo(type = errorType, exception = e)))
        }
    }

    override suspend fun storeAudioClips(audioClips: List<AudioClip>): Result<Unit> {
        db.mainDao().storeAudioClips(audioClips)
        return Result.success(Unit)
    }

    override suspend fun getLocalAudioClips(): Result<List<AudioClip>> {
        val audioClips = db.mainDao().getAudioClips()
        return Result.success(audioClips)
    }

    override suspend fun clearLocalAudioClips(): Result<Unit> {
        db.mainDao().clearAudioClips()
        return Result.success(Unit)
    }
}