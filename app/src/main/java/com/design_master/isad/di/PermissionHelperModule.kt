package com.design_master.isad.di

import android.app.Application
import com.design_master.isad.utils.helper.PermissionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PermissionHelperModule {
    @Provides
    @Singleton
    fun providePermissionHelper(application: Application): PermissionHelper = PermissionHelper()
}