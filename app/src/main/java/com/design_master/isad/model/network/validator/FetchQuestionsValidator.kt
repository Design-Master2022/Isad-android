package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.FetchQuestionsResponse
import com.design_master.isad.model.network.response.FetchQuestionsResponseClasses
import retrofit2.Response

interface FetchQuestionsValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToFetchQuestions()
    fun onQuestionsFetched(questions: List<FetchQuestionsResponseClasses.Question>)
}

class FetchQuestionsValidator {
    companion object {
        fun validate(
            response: Response<FetchQuestionsResponse>,
            callbacks: FetchQuestionsValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.data?.let {
                            callbacks.onQuestionsFetched(it.questions)
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToFetchQuestions()
                    }
                }
            }
        }
    }
}