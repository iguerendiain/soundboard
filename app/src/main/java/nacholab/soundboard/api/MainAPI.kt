package nacholab.soundboard.api

import nacholab.soundboard.domain.AudioClip
import retrofit2.Response
import retrofit2.http.GET

interface MainAPI{

    @GET("/audioclips")
    suspend fun downloadAudioClips(): Response<List<AudioClip>>

}