package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.LoginResponse
import retrofit2.Response

interface LoginValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToLogin()
    fun onLoggedIn()
}

class LoginValidator {
    companion object {
        fun validate(
            response: Response<LoginResponse>,
            callbacks: LoginValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    callbacks.onLoggedIn()
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToLogin()
                    }
                }
            }
        }
    }
}