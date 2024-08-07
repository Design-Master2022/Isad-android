package com.design_master.isad.model.network.service

import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.network.response.FetchAllSpeakersResponse
import retrofit2.Call
import retrofit2.http.GET

interface FetchAllSpeakersService {
    @GET("${Constants.API_VERSION}speakers")
    fun fetchAllSpeakers(): Call<FetchAllSpeakersResponse>
}