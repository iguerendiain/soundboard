package nacholab.soundboard.storage

import nacholab.android.errorhandling.domain.ErrorException
import nacholab.android.errorhandling.domain.ErrorInfo
import nacholab.android.errorhandling.domain.ErrorType
import nacholab.soundboard.api.MainAPI
import nacholab.soundboard.domain.MainRepository
import nacholab.soundboard.domain.AudioClip
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
                    ErrorException(ErrorInfo(type = ErrorType.EMPTY))
                )
            } else
                Result.failure(
                    ErrorException(
                        ErrorInfo(
                            type = ErrorType.UNKNOWN,
                            errorBody = audioClipsResponse.errorBody()?.string(),
                            statusCode = audioClipsResponse.code(),
                            exception = null
                        )
                    )
                )
        }catch (e: Exception){
            val errorType = when (e) {
                is IOException -> ErrorType.NETWORK
                is RuntimeException -> ErrorType.PARSE
                else -> ErrorType.UNKNOWN
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