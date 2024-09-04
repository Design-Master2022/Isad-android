package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.GetHomeDataResponse
import retrofit2.Call
import retrofit2.http.GET

interface GetHomeDataService {
    @GET("${Constants.API_VERSION}home")
    fun getHomeData()
    : Call<GetHomeDataResponse>
}