package com.design_master.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master.isad.model.constants.ApiResponseConstants

data class GetHomeDataResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: GetHomeDataResponseClasses.Data?,
    val count: Int?
)
object GetHomeDataResponseClasses{

    data class Data(
        val banners: List<Banner>,
        @SerializedName("buttons_1") val buttons1: List<Buttons1>,
        @SerializedName("about_conference") val aboutConference: List<AboutConference>,
        @SerializedName("sponsers") val sponsors: List<Sponsor>,
        val info: List<Information>?,
        val about: List<About?>,
        @SerializedName("about_kuwait") val aboutKuwait: List<AboutKuwait>,

    )
    data class Buttons1(
        @SerializedName("name_1") val name1: String,
        @SerializedName("url_1") val url1: String,
        @SerializedName("name_2") val name2: String,
        @SerializedName("url_2") val url2: String,
    )
    data class Banner(
        val id: String,
        @SerializedName("_id") val id2: String,
        val image: String,
        @SerializedName("redirect_url") val redirectUrl: String,
        val sort: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String
    )
    data class AboutConference(
        @SerializedName("conference_title") val title: String,
        @SerializedName("conference_message") val message: String,
        @SerializedName("conference_image") val image: String
    )
    data class AboutKuwait(
        @SerializedName("kuwait_title") val title: String,
        @SerializedName("kuwait_message") val message: String,
        @SerializedName("kuwait_image") val image: String
    )
    data class Sponsor(
        val id: String,
        @SerializedName("_id") val id2: String,
        val image: String
    )
    data class About(
        @SerializedName("about_message") val message: String,
        @SerializedName("about_image") val image: String
    )

    data class Information(
        val id: String,
        @SerializedName("_id") val id2: String,
        val name1: String,
        val name2: String,
        val name3: String,
        val name4: String,
        val image1: String,
        val image2: String,
        val image3: String,
        val image4: String,
        val count1: String,
        val count2: String,
        val count3: String,
        val count4: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String
    )
}

