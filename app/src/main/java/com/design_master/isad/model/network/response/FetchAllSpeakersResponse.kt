package com.design_master.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master.isad.model.constants.ApiResponseConstants

data class FetchAllSpeakersResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    @SerializedName("data") val speakers: List<FetchAllSpeakersResponseClasses.Speaker>?
)
object FetchAllSpeakersResponseClasses{

    data class Speaker(
        @SerializedName("_id") val id: String,
        val name: String,
        val country: List<Country>,
        val image: String,
        val detail: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String
    )
    data class Country(
        val id: String,
        val name: String,
        @SerializedName("country_code") val countryCode: String,
        @SerializedName("country_code3") val countryCode3: String,
        @SerializedName("flag_code") val flagCode: String
    )
}

