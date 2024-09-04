package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.network.response.LoginWithOtpResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginWithOtpService {
    @FormUrlEncoded
    @POST("restapi/postlogincheckwithemail")
    fun loginWithOtp(
        @Field("reg_email") email: String
    ): Call<LoginWithOtpResponse>
}