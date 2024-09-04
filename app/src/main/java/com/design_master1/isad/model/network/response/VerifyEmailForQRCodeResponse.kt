package com.design_master1.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master1.isad.model.constants.ApiResponseConstants

data class VerifyEmailForQRCodeResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val msg: String,
    @SerializedName("registration_code") val registrationCode: String?
)
