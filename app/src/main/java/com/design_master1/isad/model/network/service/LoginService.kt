package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.LoginResponse
import com.design_master1.isad.model.view_models.activities.SplashActivityViewModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("${Constants.API_VERSION}login")
    fun login(
        @Body loginParams: SplashActivityViewModel.LoginParams
    ): Call<LoginResponse>
}