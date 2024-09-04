package com.design_master1.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master1.isad.model.constants.ApiResponseConstants

data class FetchQuestionsResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: FetchQuestionsResponseClasses.Data?,
    val message: String?
)
class FetchQuestionsResponseClasses {
    data class Data(
        val questions: List<Question>
    )
    data class Question(
        val id: String,
        @SerializedName("_id") val id2: String,
        val question: String,
        val status: String,
        val name: String
    )
}
