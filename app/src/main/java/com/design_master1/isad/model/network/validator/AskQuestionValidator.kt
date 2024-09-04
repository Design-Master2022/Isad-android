package com.design_master1.isad.model.network.validator

import com.design_master1.isad.model.constants.ApiResponseConstants
import com.design_master1.isad.model.network.response.AskQuestionResponse
import retrofit2.Response

interface AskQuestionValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToAskQuestion()
    fun onQuestionAsked()
}

class AskQuestionValidator {
    companion object {
        fun validate(
            response: Response<AskQuestionResponse>,
            callbacks: AskQuestionValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.data?.let {
                            callbacks.onQuestionAsked()
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToAskQuestion()
                    }
                }
            }
        }
    }
}