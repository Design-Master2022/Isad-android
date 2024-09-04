package com.design_master1.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master1.isad.model.constants.ApiResponseConstants

data class FetchLocationResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: FetchLocationResponseClasses.Data?,
    val images: List<FetchLocationResponseClasses.Image>?
)
object FetchLocationResponseClasses{
    data class Data(
        val id: String,
        @SerializedName("image_url") val image: String,
        val latitude: String,
        val longitude: String,
        val description: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String
    )
    data class Image(
        @SerializedName("image_url") val imageUrl: String
    )
}

