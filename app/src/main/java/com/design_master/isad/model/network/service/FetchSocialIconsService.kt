package com.design_master.isad.model.network.service

import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.network.response.FetchMenuResponse
import com.design_master.isad.model.network.response.FetchSocialIconsResponse
import com.design_master.isad.model.network.response.RemoveFromWishlistResponse
import com.design_master.isad.model.view_models.activities.MainActivityViewModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FetchSocialIconsService {
    @GET("${Constants.API_VERSION}social-icons")
    fun fetch(): Call<FetchSocialIconsResponse>
}