package com.design_master1.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master1.isad.model.constants.ApiResponseConstants

data class FetchSocialIconsResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: List<FetchSocialIconsResponseClasses.SocialUrls>?
)
class FetchSocialIconsResponseClasses {
    data class SocialUrls(
        val facebook: String?,
        val instagram: String?,
        val twitter: String?,
        val linkedin: String?,
        val youtube: String?,
        val whatsapp: String?,
        val telegram: String?
    )
}

