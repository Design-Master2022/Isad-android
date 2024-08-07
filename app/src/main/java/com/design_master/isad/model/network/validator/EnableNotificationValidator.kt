package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.EnableNotificationResponse
import com.design_master.isad.model.network.response.EnableNotificationResponseClasses
import retrofit2.Response

interface EnableNotificationValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToEnableNotification()
    fun onNotificationEnabled(data: EnableNotificationResponseClasses.Data)
}

class EnableNotificationValidator {
    companion object {
        fun validate(
            response: Response<EnableNotificationResponse>,
            callbacks: EnableNotificationValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.data?.let {
                            callbacks.onNotificationEnabled(it)
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToEnableNotification()
                    }
                }
            }
        }
    }
}