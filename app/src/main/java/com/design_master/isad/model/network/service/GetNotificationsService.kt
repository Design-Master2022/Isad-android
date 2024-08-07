package com.design_master.isad.model.network.service

import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.network.response.GetEnabledNotificationsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetNotificationsService {
    @GET("${Constants.API_VERSION}notification/{deviceId}")
    fun getAllNotifications(
        @Path("deviceId") deviceId: String,
        @Query("populate") populate: Boolean,
    ): Call<GetEnabledNotificationsResponse>
}