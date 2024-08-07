package com.design_master.isad.model.network.response

import com.google.gson.annotations.SerializedName
import com.design_master.isad.model.constants.ApiResponseConstants

data class AskQuestionResponse (
    @SerializedName(ApiResponseConstants.RESPONSE_STATUS) val responseStatus: Int,
    val data: AskQuestionResponseClasses.Data?,
    val message: String?
)
class AskQuestionResponseClasses {
    data class Data(
        val data: Data2
    )
    data class Data2(
        val id: String,
        @SerializedName("_id") val id2: String,
        val question: String,
        val status: String,
        val name: String,
        @SerializedName("ses_name") val sessionName: String,
        @SerializedName(ApiResponseConstants.CREATED_DATE) val createdDate: String,
        @SerializedName(ApiResponseConstants.UPDATED_DATE) val updatedDate: String,
    )
}
