package com.google.dagger.di

import android.app.UiModeManager
import android.content.Context
import android.media.projection.MediaProjectionManager
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import android.os.PowerManager
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SystemServiceModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    fun provideWifiManager(context: Context): WifiManager {
        return ContextCompat.getSystemService(context, WifiManager::class.java) ?: throw IllegalArgumentException()
    }

    @Provides
    fun provideNsdManager(context: Context): NsdManager {
        return ContextCompat.getSystemService(context, NsdManager::class.java) ?: throw IllegalArgumentException()
    }

    @Provides
    fun providePowerManager(context: Context): PowerManager {
        return ContextCompat.getSystemService(context, PowerManager::class.java) ?: throw IllegalArgumentException()
    }

    @Provides
    fun provideUiModeManager(context: Context): UiModeManager {
        return ContextCompat.getSystemService(context, UiModeManager::class.java) ?: throw IllegalArgumentException()
    }

    @Provides
    fun provideMediaProjectionManager(context: Context): MediaProjectionManager {
        return ContextCompat.getSystemService(context, MediaProjectionManager::class.java) ?: throw IllegalArgumentException()
    }
}
