package com.android.appname.arch.modules

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import com.android.appname.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author mvn-vuongphan-dn 10/14/22
 */
@Module
@InstallIn(SingletonComponent::class)
class SharePreferencesModule {
    @Singleton
    @Provides
    fun provideSharePreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}_preferences",
            Context.MODE_PRIVATE
        )

    @Singleton
    @Provides
    fun provideAssets(@ApplicationContext context: Context): AssetManager = context.assets
}
