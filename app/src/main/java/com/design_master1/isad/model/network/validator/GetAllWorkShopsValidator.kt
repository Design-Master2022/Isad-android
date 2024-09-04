package com.design_master1.isad.model.network.validator

import com.design_master1.isad.model.constants.ApiResponseConstants
import com.design_master1.isad.model.network.response.GetAllWorkShopsResponse
import com.design_master1.isad.model.network.response.GetAllWorkShopsResponseClasses
import retrofit2.Response

interface GetAllWorkShopsValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToFetchWorkShops()
    fun onWorkShopsFetched(workShopData: GetAllWorkShopsResponseClasses.Data)
}

class GetAllWorkShopsValidator {
    companion object {
        fun validate(
            response: Response<GetAllWorkShopsResponse>,
            callbacks: GetAllWorkShopsValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.data?.let {
                            callbacks.onWorkShopsFetched(it.first())
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToFetchWorkShops()
                    }
                }
            }
        }
    }
}