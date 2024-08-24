package nacholab.soundboard.di

import android.app.Application
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nacholab.soundboard.BuildConfig
import nacholab.soundboard.api.MainAPI
import nacholab.soundboard.api.MainAPIBuilder
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Provides
    @Singleton
    fun provideMainApi(
        app: Application,
        flipperInterceptor: FlipperOkhttpInterceptor?
    ): MainAPI {
        val interceptors = mutableListOf<Interceptor>()
        flipperInterceptor?.let { interceptors.add(it) }
        if (BuildConfig.DEBUG)
            interceptors.add(HttpLoggingInterceptor())

        return MainAPIBuilder.build(
            app.cacheDir,
            BuildConfig.API_CACHE,
            interceptors.toTypedArray()
        )
    }

}