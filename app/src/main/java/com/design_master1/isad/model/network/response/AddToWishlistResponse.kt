package com.design_master1.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master1.isad.model.constants.ApiResponseConstants

data class AddToWishlistResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: AddToWishlistResponseClasses.Data?,
    val message: String?
)
object AddToWishlistResponseClasses{
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

