package com.design_master.isad.di

import android.app.Application
import android.content.Context
import com.design_master.isad.utils.prefs_controller.PrefsController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PrefsControllerModule {
    @Provides
    @Singleton
    fun providePrefsController(application: Application): PrefsController {
        return PrefsController(
            application.getSharedPreferences("isad_prefs_controller", Context.MODE_PRIVATE)
        )
    }
}