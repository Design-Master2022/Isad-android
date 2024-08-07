package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.GetEnabledNotificationsResponse
import com.design_master.isad.model.network.response.GetEnabledNotificationsResponseClasses
import retrofit2.Response

interface GetNotificationValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToFetchNotifications()
    fun onNotificationsFetched(notifications: List<GetEnabledNotificationsResponseClasses.Notification>)
}

class GetNotificationValidator {
    companion object {
        fun validate(
            response: Response<GetEnabledNotificationsResponse>,
            callbacks: GetNotificationValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.notifications?.let {
                            callbacks.onNotificationsFetched(it)
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToFetchNotifications()
                    }
                }
            }
        }
    }
}