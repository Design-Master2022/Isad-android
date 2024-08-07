package com.design_master.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master.isad.model.constants.ApiResponseConstants

data class SendWelcomeNotificationResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: List<SendWelcomeNotificationResponseClasses.Data>?,
    val count: Int?
)
object SendWelcomeNotificationResponseClasses{
    data class Data(
        @SerializedName("multicast_id") val multicastId: Long,
        val success: Int,
        val failure: Int,
        @SerializedName("canonical_ids") val canonicalId: String,
        val results: List<Result>
    )
    data class Result(
        @SerializedName("message_id") val messageId: String
    )
}

