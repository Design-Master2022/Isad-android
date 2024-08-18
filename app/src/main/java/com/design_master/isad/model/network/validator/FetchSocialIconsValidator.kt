package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.AddToWishlistResponse
import com.design_master.isad.model.network.response.AddToWishlistResponseClasses
import com.design_master.isad.model.network.response.FetchSocialIconsResponse
import com.design_master.isad.model.network.response.FetchSocialIconsResponseClasses
import retrofit2.Response

interface FetchSocialIconsValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToFetch()
    fun onSocialIconsFetched(data: FetchSocialIconsResponseClasses.SocialUrls)
}

class FetchSocialIconsValidator {
    companion object {
        fun validate(
            response: Response<FetchSocialIconsResponse>,
            callbacks: FetchSocialIconsValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.data?.let {
                            if (it.isNotEmpty()) callbacks.onSocialIconsFetched(it.first())
                            else callbacks.onFailedToFetch()
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToFetch()
                    }
                }
            }
        }
    }
}