package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.GetHomeDataResponse
import com.design_master.isad.model.network.response.GetHomeDataResponseClasses
import retrofit2.Response

interface GetHomeDataValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToFetchHomeData()
    fun onHomeDataFetched(data: GetHomeDataResponseClasses.Data)
}

class GetHomeDataValidator {
    companion object {
        fun validate(
            response: Response<GetHomeDataResponse>,
            callbacks: GetHomeDataValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.data?.let {
                            callbacks.onHomeDataFetched(it)
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToFetchHomeData()
                    }
                }
            }
        }
    }
}