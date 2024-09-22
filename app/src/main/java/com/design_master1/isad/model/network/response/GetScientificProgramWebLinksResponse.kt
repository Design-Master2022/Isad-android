package com.design_master1.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master1.isad.model.constants.ApiResponseConstants

data class GetScientificProgramWebLinksResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: GetScientificProgramWebLinksResponseClasses.Data?,
    val count: Int?
)
object GetScientificProgramWebLinksResponseClasses{
    data class Data(
        @SerializedName("day_1") val day1: String,
        @SerializedName("day_2") val day2: String,
        @SerializedName("day_3") val day3: String,
    )
}

