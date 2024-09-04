package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.AskQuestionResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AskQuestionService{
    @POST("${Constants.API_VERSION}ask")
    fun ask(
        @Body params: AskQuestionParams
    ): Call<AskQuestionResponse>
}
data class AskQuestionParams(
    val deviceId: String,
    val name: String,
    @SerializedName("ses_name") val sessionName: String,
    val question: String
)