package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.GetAllScientificProgramsResponse
import com.design_master.isad.model.network.response.GetAllScientificProgramsResponseClasses
import retrofit2.Response

interface GetAllScientificProgramsValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToFetchScientificPrograms()
    fun onScientificProgramsFetched(programsData: GetAllScientificProgramsResponseClasses.Data)
}

class GetAllScientificProgramsValidator {
    companion object {
        fun validate(
            response: Response<GetAllScientificProgramsResponse>,
            callbacks: GetAllScientificProgramsValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.data?.let {
                            callbacks.onScientificProgramsFetched(it.first())
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToFetchScientificPrograms()
                    }
                }
            }
        }
    }
}