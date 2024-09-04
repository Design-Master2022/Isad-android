package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.FetchSocialIconsResponse
import retrofit2.Call
import retrofit2.http.GET

interface FetchSocialIconsService {
    @GET("${Constants.API_VERSION}social-icons")
    fun fetch(): Call<FetchSocialIconsResponse>
}