package com.design_master1.isad.model.network.validator

import com.design_master1.isad.model.constants.ApiResponseConstants
import com.design_master1.isad.model.network.response.VerifyOtpResponse
import retrofit2.Response

interface VerifyOtpValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToVerifyOtp()
    fun onWrongOtp()
    fun onOtpVerified(cmeUrl: String, videoUrl: String)
}

class VerifyOtpValidator {
    companion object {
        fun validate(
            response: Response<VerifyOtpResponse>,
            callbacks: VerifyOtpValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { response ->
                        if (response.responseStatus == 1) callbacks.onOtpVerified(
                            cmeUrl = response.cmeUrl?:"",
                            videoUrl = response.videoUrl?:""
                        )
                        else if (response.msg.contains("Invalid")) callbacks.onWrongOtp()
                        else callbacks.onFailedToVerifyOtp()
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToVerifyOtp()
                    }
                }
            }
        }
    }
}