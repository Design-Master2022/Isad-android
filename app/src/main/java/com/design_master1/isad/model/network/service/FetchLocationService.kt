package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.FetchLocationResponse
import retrofit2.Call
import retrofit2.http.GET

interface FetchLocationService {
    @GET("${Constants.API_VERSION}event_location")
    fun fetch(): Call<FetchLocationResponse>
}