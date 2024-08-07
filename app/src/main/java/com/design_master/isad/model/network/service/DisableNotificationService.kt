package com.design_master.isad.model.network.service

import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.network.response.DisableNotificationResponse
import com.design_master.isad.model.view_models.activities.MainActivityViewModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface DisableNotificationService {
    @POST("${Constants.API_VERSION}notification-delete/{id}?populate=true")
    fun disableNotification(
        @Path("id") id: String,
        @Body deviceId: MainActivityViewModel.DeviceIdParam
    ): Call<DisableNotificationResponse>
}