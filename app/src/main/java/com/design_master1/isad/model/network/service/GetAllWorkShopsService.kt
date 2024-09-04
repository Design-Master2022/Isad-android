package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.GetAllWorkShopsResponse
import com.design_master1.isad.model.view_models.fragments.WorkShopsFragmentViewModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
interface GetAllWorkShopsService {
    @POST("${Constants.API_VERSION}workshops")
    fun getAllShops(
        @Query("populate") populate: Boolean,
        @Body params: WorkShopsFragmentViewModel.WorkShopParams
    ): Call<GetAllWorkShopsResponse>
}