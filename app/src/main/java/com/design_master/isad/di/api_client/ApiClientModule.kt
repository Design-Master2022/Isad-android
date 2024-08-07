package com.design_master.isad.di.api_client

import com.design_master.isad.model.network.client.ApiClient
import com.design_master.isad.model.network.service.AddFeedbackService
import com.design_master.isad.model.network.service.AddToWishlistService
import com.design_master.isad.model.network.service.AskQuestionService
import com.design_master.isad.model.network.service.DisableNotificationService
import com.design_master.isad.model.network.service.EnableNotificationService
import com.design_master.isad.model.network.service.FetchAllSpeakersService
import com.design_master.isad.model.network.service.FetchLocationService
import com.design_master.isad.model.network.service.FetchQuestionsService
import com.design_master.isad.model.network.service.GetNotificationsService
import com.design_master.isad.model.network.service.GetAllScientificProgramsService
import com.design_master.isad.model.network.service.GetAllWorkShopsService
import com.design_master.isad.model.network.service.GetHomeDataService
import com.design_master.isad.model.network.service.GetWishlistService
import com.design_master.isad.model.network.service.LoginService
import com.design_master.isad.model.network.service.LoginWithOtpService
import com.design_master.isad.model.network.service.RemoveFromWishlistService
import com.design_master.isad.model.network.service.SaveNotesService
import com.design_master.isad.model.network.service.SendWelcomeNotificationService
import com.design_master.isad.model.network.service.VerifyOtpService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiClientModule {
    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): ApiClient {
        return ApiClient(
            retrofit.create(GetAllScientificProgramsService::class.java),
            retrofit.create(GetHomeDataService::class.java),
            retrofit.create(GetAllWorkShopsService::class.java),
            retrofit.create(GetWishlistService::class.java),
            retrofit.create(LoginService::class.java),
            retrofit.create(SaveNotesService::class.java),
            retrofit.create(AddToWishlistService::class.java),
            retrofit.create(RemoveFromWishlistService::class.java),
            retrofit.create(EnableNotificationService::class.java),
            retrofit.create(DisableNotificationService::class.java),
            retrofit.create(GetNotificationsService::class.java),
            retrofit.create(FetchAllSpeakersService::class.java),
            retrofit.create(SendWelcomeNotificationService::class.java),
            retrofit.create(LoginWithOtpService::class.java),
            retrofit.create(VerifyOtpService::class.java),
            retrofit.create(FetchLocationService::class.java),
            retrofit.create(AddFeedbackService::class.java),
            retrofit.create(FetchQuestionsService::class.java),
            retrofit.create(AskQuestionService::class.java)
        )
    }
}