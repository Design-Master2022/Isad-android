package com.design_master.isad.model.network.service

import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.network.response.AddFeedbackResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AddFeedbackService {
    @POST("${Constants.API_VERSION}feedback")
    fun add(
        @Body params: AddFeedbackParams
    ): Call<AddFeedbackResponse>
}
data class AddFeedbackParams(
    val deviceId: String,
    val name: String,
    val email: String,
    val phone: String,
    val comment: String
)