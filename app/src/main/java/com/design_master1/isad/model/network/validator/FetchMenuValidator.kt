package com.design_master1.isad.model.network.validator

import com.design_master1.isad.model.constants.ApiResponseConstants
import com.design_master1.isad.model.network.response.FetchMenuResponse
import com.design_master1.isad.model.network.response.FetchMenuResponseClasses
import retrofit2.Response

interface FetchMenuValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToFetchMenu()
    fun onMenuFetched(data: List<FetchMenuResponseClasses.MenuItem>)
}

class FetchMenuValidator {
    companion object {
        fun validate(
            response: Response<FetchMenuResponse>,
            callbacks: FetchMenuValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        if (trendalResponse.data != null)
                            callbacks.onMenuFetched(trendalResponse.data)
                        else
                            callbacks.onFailedToFetchMenu()
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToFetchMenu()
                    }
                }
            }
        }
    }
}