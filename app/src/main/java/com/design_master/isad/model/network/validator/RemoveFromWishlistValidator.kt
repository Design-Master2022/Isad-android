package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.RemoveFromWishlistResponse
import retrofit2.Response

interface RemoveFromWishlistValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToRemoveFromWishlist()
    fun onRemovedFromWishlist()
}

class RemoveFromWishlistValidator {
    companion object {
        fun validate(
            response: Response<RemoveFromWishlistResponse>,
            callbacks: RemoveFromWishlistValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.message?.let {
                            if (it.contains("Deleted")) callbacks.onRemovedFromWishlist()
                            else callbacks.onFailedToRemoveFromWishlist()
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToRemoveFromWishlist()
                    }
                }
            }
        }
    }
}