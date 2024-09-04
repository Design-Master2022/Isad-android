package com.design_master1.isad.model.network.validator

import com.design_master1.isad.model.constants.ApiResponseConstants
import com.design_master1.isad.model.network.response.SendWelcomeNotificationResponse
import retrofit2.Response

interface SendWelcomeNotificationValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToSendNotification()
    fun onOutOfArea()
    fun onResponseFailure()
    fun onNotificationSent()
}

class SendWelcomeNotificationValidator {
    companion object {
        fun validate(
            response: Response<SendWelcomeNotificationResponse>,
            callbacks: SendWelcomeNotificationValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        if (trendalResponse.responseStatus == 200){
                            trendalResponse.data?.let {
                                if (it.first().success == 1) callbacks.onNotificationSent()
                                else if (it.first().failure == 1) callbacks.onOutOfArea()
                                else callbacks.onFailedToSendNotification()
                            }
                        }else{
                            callbacks.onResponseFailure()
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onResponseFailure()
                    }
                }
            }
        }
    }
}