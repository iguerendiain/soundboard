package nacholab.soundboard.di

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.core.FlipperClient
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FlipperModule {

    @Provides
    @Singleton
    fun provideFlipperClient(app: Application): FlipperClient? {
        return if (FlipperUtils.shouldEnableFlipper(app)) {
            SoLoader.init(app, false)
            AndroidFlipperClient.getInstance(app)?.apply {
                start()
                addPlugin(DatabasesFlipperPlugin(app))
                addPlugin(InspectorFlipperPlugin(app, DescriptorMapping.withDefaults()))
            }
        }else null
    }

    @Provides
    @Singleton
    fun provideFlipperNetworkInterceptor(flipperClient: FlipperClient?): FlipperOkhttpInterceptor? {
        return flipperClient?.let {
            val plugin = NetworkFlipperPlugin()
            it.addPlugin(plugin)
            FlipperOkhttpInterceptor(plugin)
        }
    }
}