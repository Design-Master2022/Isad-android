package com.design_master1.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master1.isad.model.constants.ApiResponseConstants

data class GetWishlistResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    @SerializedName("data") val wishlist: List<GetWishlistResponseClasses.WishlistItem>?
)
object GetWishlistResponseClasses{
    data class WishlistItem(
        @SerializedName("_id") val id: String,
        val deviceId: String,
        val programOrWorkshopId: String,
        val dayWiseId: String,
        val type: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName("programOrworkshop") val programOrWorkShop: ProgramOrWorkShop,
        @SerializedName("day_wise") val dayWise: DayWise
    )
    data class ProgramOrWorkShop(
        val id: String,
        @SerializedName("_id") val id2: String,
        @SerializedName("start_date") val startDate: String,
        val days: String,
        val title: String,
        val notes: String,
        val image: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String,
    )
    data class DayWise(
        val id: String,
        @SerializedName("_id") val id2: String,
        val day: String,
        val type: String,
        @SerializedName("time_from") val timeFrom: String,
        @SerializedName("time_to") val timeTo: String,
        val title: String,
        val image: String,
        @SerializedName("main_id") val mainId: String,
        val speaker: Speaker,
        val notes: String,
        @SerializedName("vs_speaker") val vsSpeaker: VsSpeaker?,
        val track: String?,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String,
        val opening: String,
        var notification: Boolean
    )
    data class VsSpeaker(
        val id: String,
        @SerializedName("_id") val id2: String,
        val name: String,
        val country: String,
        val image: String,
        val detail: String,
        val speaker: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String
    )
    data class Speaker(
        val id: String,
        @SerializedName("_id") val id2: String,
        val name: String,
        val country: String,
        val image: String,
        val detail: String,
        @SerializedName("speaker") val isSpeaker: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String
    )

}

