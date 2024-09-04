package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.network.response.VerifyEmailForQRCodeResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface VerifyEmailForQRCodeService {
    @FormUrlEncoded
    @POST("restapi/postverifyvalidemailaddress")
    fun verify(
        @Field("reg_email") email: String
    ): Call<VerifyEmailForQRCodeResponse>
}