package com.design_master.isad.model.network.service

import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.network.response.SendWelcomeNotificationResponse
import com.design_master.isad.service.LocationUpdateService
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SendWelcomeNotificationService {
    @POST("${Constants.API_VERSION}alert-geo-lat-lon")
    fun sendNotification(
        @Body params: LocationUpdateService.SendWelcomeNotificationParams
    ): Call<SendWelcomeNotificationResponse>
}