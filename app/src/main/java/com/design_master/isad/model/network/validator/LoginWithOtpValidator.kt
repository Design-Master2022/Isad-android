package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.LoginWithOtpResponse
import retrofit2.Response
interface LoginWithOtpValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToSendOtp()
    fun onOtpSent()
}

class LoginWithOtpValidator {
    companion object {
        fun validate(
            response: Response<LoginWithOtpResponse>,
            callbacks: LoginWithOtpValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        if (trendalResponse.responseStatus == 1) callbacks.onOtpSent()
                        else callbacks.onFailedToSendOtp()
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToSendOtp()
                    }
                }
            }
        }
    }
}