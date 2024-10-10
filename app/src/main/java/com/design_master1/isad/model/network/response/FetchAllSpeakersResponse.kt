package com.design_master1.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master1.isad.model.constants.ApiResponseConstants

data class FetchAllSpeakersResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    @SerializedName("data") val data: FetchAllSpeakersResponseClasses.Data?
)
object FetchAllSpeakersResponseClasses{

    data class Data(
        val speakers: List<Speaker>
    )
    data class Speaker(
        @SerializedName("_id") val id: String,
        val name: String,
        val image: String,
        @SerializedName("items") val country: List<Country>
    )
    data class Country(
        val detail: String,
        @SerializedName("country_name") val countryName: String,
        @SerializedName("country_code") val countryCode: String
    )
}

