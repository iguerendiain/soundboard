package nacholab.soundboard.di

import android.app.Application
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaPlayerModule {

    @Provides
    @Singleton
    fun providesMediaPlayer(
        app: Application
    ): ExoPlayer {
        return ExoPlayer
            .Builder(app)
            .build()
    }

}