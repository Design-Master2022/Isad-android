package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.FetchQuestionsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FetchQuestionsService{
    @POST("${Constants.API_VERSION}my_questions")
    fun fetch(
        @Body params: FetchQuestionsParams
    ): Call<FetchQuestionsResponse>
}
data class FetchQuestionsParams(
    val deviceId: String
)