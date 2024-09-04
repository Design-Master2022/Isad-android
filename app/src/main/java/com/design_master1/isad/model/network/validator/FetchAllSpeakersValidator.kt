package com.design_master1.isad.model.network.validator

import com.design_master1.isad.model.constants.ApiResponseConstants
import com.design_master1.isad.model.network.response.FetchAllSpeakersResponse
import com.design_master1.isad.model.network.response.FetchAllSpeakersResponseClasses
import retrofit2.Response

interface FetchAllSpeakersValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToFetchSpeakers()
    fun onSpeakersFetched(speakers: List<FetchAllSpeakersResponseClasses.Speaker>)
}

class FetchAllSpeakersValidator {
    companion object {
        fun validate(
            response: Response<FetchAllSpeakersResponse>,
            callbacks: FetchAllSpeakersValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.speakers?.let {
                            callbacks.onSpeakersFetched(it)
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToFetchSpeakers()
                    }
                }
            }
        }
    }
}