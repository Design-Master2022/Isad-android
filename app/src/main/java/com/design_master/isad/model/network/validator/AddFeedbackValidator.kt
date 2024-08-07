package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.AddFeedbackResponse
import retrofit2.Response

interface AddFeedbackValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToAddFeedback()
    fun onFeedbackAdded()
}

class AddFeedbackValidator {
    companion object {
        fun validate(
            response: Response<AddFeedbackResponse>,
            callbacks: AddFeedbackValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        if (trendalResponse.message != null && trendalResponse.message.contains("added",ignoreCase = true))
                            callbacks.onFeedbackAdded()
                        else
                            callbacks.onFailedToAddFeedback()
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToAddFeedback()
                    }
                }
            }
        }
    }
}