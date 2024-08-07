package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.VerifyEmailForQRCodeResponse
import retrofit2.Response
interface VerifyEmailForQRCodeValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToVerify()
    fun onEmailVerified(registrationCode: String)
}

class VerifyEmailForQRCodeValidator {
    companion object {
        fun validate(
            response: Response<VerifyEmailForQRCodeResponse>,
            callbacks: VerifyEmailForQRCodeValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { response ->
                        if (response.responseStatus == 1)
                            if (response.registrationCode != null) callbacks.onEmailVerified(response.registrationCode)
                            else callbacks.onFailedToVerify()
                        else callbacks.onFailedToVerify()
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToVerify()
                    }
                }
            }
        }
    }
}