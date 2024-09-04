package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.GetWishlistResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetWishlistService {
    @GET("${Constants.API_VERSION}wishlist/{deviceId}")
    fun getWishlist(
        @Path("deviceId") deviceId: String,
        @Query("populate") populate: Boolean
    ): Call<GetWishlistResponse>
}