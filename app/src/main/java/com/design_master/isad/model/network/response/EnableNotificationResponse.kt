package com.design_master.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master.isad.model.constants.ApiResponseConstants

data class EnableNotificationResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: EnableNotificationResponseClasses.Data?,
    val message: String?
)
object EnableNotificationResponseClasses{
    data class Data(
        @SerializedName("data") val programOrWorkShopData: ProgramOrWorkShopData
    )
    data class ProgramOrWorkShopData(
        val id: String,
        @SerializedName("_id") val id2: String,
        val deviceId: String,
        val programOrWorkshopId: String,
        val dayWiseId: String,
        val type: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String
    )
}

