package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.FetchMenuResponse
import retrofit2.Call
import retrofit2.http.GET

interface FetchMenuService {
    @GET("${Constants.API_VERSION}menu")
    fun fetch(): Call<FetchMenuResponse>
}