package com.design_master1.isad.model.network.validator

import com.design_master1.isad.model.constants.ApiResponseConstants
import com.design_master1.isad.model.network.response.DisableNotificationResponse
import retrofit2.Response

interface DisableNotificationValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToDisableNotification()
    fun onNotificationDisabled()
}

class DisableNotificationValidator {
    companion object {
        fun validate(
            response: Response<DisableNotificationResponse>,
            callbacks: DisableNotificationValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.message?.let {
                            if (it.contains("Deleted")) callbacks.onNotificationDisabled()
                            else callbacks.onNotificationDisabled()
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onNotificationDisabled()
                    }
                }
            }
        }
    }
}