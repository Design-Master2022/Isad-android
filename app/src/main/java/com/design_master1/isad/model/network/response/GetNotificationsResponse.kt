package com.design_master1.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master1.isad.model.constants.ApiResponseConstants

data class GetEnabledNotificationsResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    @SerializedName("data") val notifications: List<GetEnabledNotificationsResponseClasses.Notification>?
)
object GetEnabledNotificationsResponseClasses{
    data class Notification(
        val id: String,
        @SerializedName("_id") val id2: String,
        val deviceId: String,
        val programOrWorkshopId: String,
        val dayWiseId: String,
        val type: String,
        val title: String,
        val message: String,
        @SerializedName("msg_type") val messageType: String,
        val status: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String
    )
}

