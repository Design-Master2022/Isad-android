package com.design_master.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master.isad.model.constants.ApiResponseConstants

data class FetchMenuResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: List<FetchMenuResponseClasses.MenuItem>?
)
class FetchMenuResponseClasses {
    data class MenuItem(
        val name: String,
        val image: String,
        @SerializedName("redirect_url") val redirectUrl: String
    )
}

