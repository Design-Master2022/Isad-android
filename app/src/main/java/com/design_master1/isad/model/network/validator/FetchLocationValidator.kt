package com.design_master1.isad.model.network.validator

import com.design_master1.isad.model.constants.ApiResponseConstants
import com.design_master1.isad.model.network.response.FetchLocationResponse
import com.design_master1.isad.model.network.response.FetchLocationResponseClasses
import retrofit2.Response

interface FetchLocationValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToFetchLocation()
    fun onLocationFetched(data: FetchLocationResponseClasses.Data, images: List<FetchLocationResponseClasses.Image>)
}

class FetchLocationValidator {
    companion object {
        fun validate(
            response: Response<FetchLocationResponse>,
            callbacks: FetchLocationValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        if (trendalResponse.data != null && trendalResponse.images != null) callbacks.onLocationFetched(trendalResponse.data, trendalResponse.images)
                        else callbacks.onFailedToFetchLocation()
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToFetchLocation()
                    }
                }
            }
        }
    }
}