package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.AddToWishlistResponse
import com.design_master1.isad.model.view_models.activities.MainActivityViewModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AddToWishlistService {
    @POST("${Constants.API_VERSION}wishlist")
    fun addToWishlist(
        @Body params: MainActivityViewModel.AddToWishlistParams
    ): Call<AddToWishlistResponse>
}