package com.design_master.isad.model.network.service

import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.network.response.LoginResponse
import com.design_master.isad.model.view_models.activities.SplashActivityViewModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("${Constants.API_VERSION}login")
    fun login(
        @Body loginParams: SplashActivityViewModel.LoginParams
    ): Call<LoginResponse>
}