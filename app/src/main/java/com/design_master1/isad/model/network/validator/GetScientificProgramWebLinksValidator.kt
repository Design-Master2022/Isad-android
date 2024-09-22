package com.design_master1.isad.model.network.validator

import com.design_master1.isad.model.constants.ApiResponseConstants
import com.design_master1.isad.model.network.response.GetAllScientificProgramsResponse
import com.design_master1.isad.model.network.response.GetAllScientificProgramsResponseClasses
import com.design_master1.isad.model.network.response.GetScientificProgramWebLinksResponse
import com.design_master1.isad.model.network.response.GetScientificProgramWebLinksResponseClasses
import retrofit2.Response

interface GetScientificProgramWebLinksValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToFetchScientificProgramWebLinks()
    fun onScientificProgramWebLinksFetched(data: GetScientificProgramWebLinksResponseClasses.Data)
}

class GetScientificProgramWebLinksValidator {
    companion object {
        fun validate(
            response: Response<GetScientificProgramWebLinksResponse>,
            callbacks: GetScientificProgramWebLinksValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.data?.let {
                            callbacks.onScientificProgramWebLinksFetched(it)
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToFetchScientificProgramWebLinks()
                    }
                }
            }
        }
    }
}