package nacholab.soundboard.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nacholab.soundboard.BuildConfig
import nacholab.soundboard.storage.MainDB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideMainDB(app: Application): MainDB {
        return Room.databaseBuilder(
            app,
            MainDB::class.java, BuildConfig.DB_NAME
        ).build()
    }
}