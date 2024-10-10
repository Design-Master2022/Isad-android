package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.FetchAllSpeakersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FetchAllSpeakersService {
//    @GET("${Constants.API_VERSION}speakers")
    @GET("${Constants.API_VERSION}committe/{type}")
    fun fetchAllSpeakers(
        @Path("type") type: Int
    ): Call<FetchAllSpeakersResponse>
}