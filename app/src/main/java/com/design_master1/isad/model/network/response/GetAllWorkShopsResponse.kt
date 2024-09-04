package com.design_master1.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master1.isad.model.constants.ApiResponseConstants

data class GetAllWorkShopsResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: List<GetAllWorkShopsResponseClasses.Data>?,
    val count: Int?
)
object GetAllWorkShopsResponseClasses{

    data class Data(
        val title: String,
        val notes: String,
        @SerializedName("_id") val id2: String,
        val days: String,
        @SerializedName("start_date") val startDate: String,
        val image: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String,
        @SerializedName("day_wise") val dayWise: DayWise
    )
    data class DayWise(
        @SerializedName("day3") val day3WorkShops: List<WorkShop>,
        @SerializedName("day2") val day2WorkShops: List<WorkShop>,
        @SerializedName("day1") val day1WorkShops: List<WorkShop>,
        @SerializedName("day1track") val day1Tracks: List<GetAllScientificProgramsResponseClasses.Track>,
        @SerializedName("day2track") val day2Tracks: List<GetAllScientificProgramsResponseClasses.Track>,
        @SerializedName("day3track") val day3Tracks: List<GetAllScientificProgramsResponseClasses.Track>
    )
    data class WorkShop(
        val id: String,
        @SerializedName("_id") val id2: String,
        val day: String,
        val type: String,
        @SerializedName("time_from") val timeFrom: String,
        @SerializedName("time_to") val timeTo: String,
        val title: String,
        val image: String,
        @SerializedName("main_id") val mainId: String,
        val speaker: Speaker,
        val notes: String,
        @SerializedName("vs_speaker") val vsSpeaker: VsSpeaker?,
        val track: String?,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String,
        val opening: String,
        var wishlist: Boolean,
        var notification: Boolean,
        @SerializedName("usernotes") val userNotes: UserNotes?
    )
    data class VsSpeaker(
        val id: String,
        @SerializedName("_id") val id2: String,
        val name: String,
        val country: String,
        val image: String,
        val detail: String,
        val speaker: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String
    )
    data class Track(
        val title1: String,
        val title2: String,
        val track: String
    )
    data class UserNotes(
        val id: String,
        @SerializedName("id_") val id2: String,
        val notes: String,
        val deviceId: String,
        val type: String,
        val programOrWorkshopId: String,
        val dayWiseId: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String
    )
    data class Speaker(
        val id: String,
        @SerializedName("_id") val id2: String,
        val name: String,
        val country: String,
        val image: String,
        val detail: String,
        @SerializedName("speaker") val isSpeaker: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String
    )
}

