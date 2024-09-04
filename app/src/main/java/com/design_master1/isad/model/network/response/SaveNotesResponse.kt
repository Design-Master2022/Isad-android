package com.design_master1.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master1.isad.model.constants.ApiResponseConstants

data class SaveNotesResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: SaveNotesResponseClasses.Data?,
    val message: String?
)
object SaveNotesResponseClasses{
    data class Data(
        @SerializedName("data") val note: Note,
        val count: Int?
    )
    data class Note(
        val id: String,
        @SerializedName("_id") val id2: String,
        val notes: String,
        val deviceId: String,
        val type: String,
        val programOrWorkshopId: String,
        val dayWiseId: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String
    )
}

