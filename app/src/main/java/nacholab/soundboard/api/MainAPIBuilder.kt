package nacholab.soundboard.api

import com.google.gson.GsonBuilder
import nacholab.soundboard.BuildConfig
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object MainAPIBuilder{

    fun build(
        cacheDir: File,
        cacheSize: Long,
        extraInterceptors: Array<Interceptor>
    ): MainAPI {
        val okHttpClientBuilder = OkHttpClient.Builder()

        if (!BuildConfig.DEBUG) okHttpClientBuilder.cache(Cache(cacheDir, cacheSize))

        extraInterceptors.forEach { okHttpClientBuilder.addNetworkInterceptor(it) }

        val converterFactory = GsonConverterFactory.create(GsonBuilder().create())

        val okHttpClient = okHttpClientBuilder
            .readTimeout(BuildConfig.API_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
            .create(MainAPI::class.java)
    }
}