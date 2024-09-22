package com.design_master1.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master1.isad.model.constants.ApiResponseConstants

data class GetHomeDataResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: GetHomeDataResponseClasses.Data?,
    val count: Int?
)
object GetHomeDataResponseClasses{

    data class Data(
        @SerializedName("isad_array_1") val banners: List<Banner>,
        @SerializedName("isad_array_2") val buttons: List<Button>,
        @SerializedName("isad_array_8") val socialButtons: SocialButtons,
        @SerializedName("isad_array_3") val heading: List<Heading>,
        @SerializedName("isad_array_4") val personInfo1: List<PersonInfo>,
        @SerializedName("isad_array_5") val moreInfo: List<MoreInfo>,
        @SerializedName("isad_array_6") val information: List<Information>,
        @SerializedName("isad_array_7") val footer: List<Footer>
    )
    data class SocialButtons(
        @SerializedName("button_3") val weChatButton: Button,
        @SerializedName("button_4") val whatsAppButton: Button,
        @SerializedName("button_5") val telegramButton: Button,
    )
    data class Button(
       val name: String,
       val url: String
    )
    data class Heading(
        val info: String
    )
    data class Banner(
        val title: String,
        val logo: String,
        @SerializedName("sub_title") val subTitle: String,
        val date: String,
        val place: String,
    )
    data class PersonInfo(
        val title: String,
        val info: String,
        val img: String
    )
    data class MoreInfo(
        val info: String,
        @SerializedName("info_1") val info1: String,
        val img: String,
        @SerializedName("read_more") val readMore: String
    )
    data class Footer(
        @SerializedName("footer_title") val title: String,
        @SerializedName("footer_message") val message: String,
        @SerializedName("footer_image") val image: String
    )

    data class Information(
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
        val count4: String
    )
}

