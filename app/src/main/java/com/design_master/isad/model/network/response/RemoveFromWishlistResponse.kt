package com.design_master.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master.isad.model.constants.ApiResponseConstants

data class RemoveFromWishlistResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val message: String?
)

