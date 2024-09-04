package com.design_master1.isad.di

import android.app.Application
import com.design_master1.isad.utils.helper.Helper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HelperModule {
    @Provides
    @Singleton
    fun provideHelper(application: Application) = Helper(application)
}