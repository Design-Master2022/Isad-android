package com.design_master.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master.isad.model.constants.ApiResponseConstants

data class GetAllScientificProgramsResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: List<GetAllScientificProgramsResponseClasses.Data>?,
    val count: Int?
)
object GetAllScientificProgramsResponseClasses{
    data class Data(
        @SerializedName("_id") val id2: String,
        @SerializedName("start_date") val startDate: String,
        val days: String,
        val title: String,
        val notes: String,
        val image: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String,
        @SerializedName("day_wise") val dayWise: DayWise
    )
    data class DayWise(
        @SerializedName("day3") val day3Programs: List<ScientificProgram>,
        @SerializedName("day2") val day2Programs: List<ScientificProgram>,
        @SerializedName("day1") val day1Programs: List<ScientificProgram>,
        @SerializedName("day1track") val day1Tracks: List<Track>,
        @SerializedName("day2track") val day2Tracks: List<Track>,
        @SerializedName("day3track") val day3Tracks: List<Track>
    )
    data class ScientificProgram(
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
        @SerializedName("id_") val id2: String,
        val name: String,
        val country: String,
        val image: String,
        val detail: String,
        @SerializedName("speaker") val isSpeaker: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String
    )
}

