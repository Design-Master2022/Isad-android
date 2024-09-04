package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.RemoveFromWishlistResponse
import com.design_master1.isad.model.view_models.activities.MainActivityViewModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface RemoveFromWishlistService {
    @POST("${Constants.API_VERSION}wishlist-delete/{dayWiseId}")
    fun removeFromWishlist(
        @Path("dayWiseId") dayWiseId: String,
        @Body deviceId: MainActivityViewModel.DeviceIdParam
    ): Call<RemoveFromWishlistResponse>
}