package com.design_master.isad.model.network.service

import com.design_master.isad.model.network.response.VerifyOtpResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface VerifyOtpService {
    @FormUrlEncoded
    @POST("restapi/postverifyemailwithotp")
    fun verify(
        @Field("reg_email") email: String,
        @Field("reg_otp") otp: String
    ): Call<VerifyOtpResponse>
}