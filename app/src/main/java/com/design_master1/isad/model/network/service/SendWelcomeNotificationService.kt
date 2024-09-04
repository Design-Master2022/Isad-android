package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.SendWelcomeNotificationResponse
import com.design_master1.isad.service.LocationUpdateService
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SendWelcomeNotificationService {
    @POST("${Constants.API_VERSION}alert-geo-lat-lon")
    fun sendNotification(
        @Body params: LocationUpdateService.SendWelcomeNotificationParams
    ): Call<SendWelcomeNotificationResponse>
}