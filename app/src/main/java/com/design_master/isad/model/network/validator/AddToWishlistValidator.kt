package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.AddToWishlistResponse
import com.design_master.isad.model.network.response.AddToWishlistResponseClasses
import retrofit2.Response

interface AddToWishlistValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToAddToWishlist()
    fun onAddedToWiGetWishlist(data: AddToWishlistResponseClasses.Data)
}

class AddToWishlistValidator {
    companion object {
        fun validate(
            response: Response<AddToWishlistResponse>,
            callbacks: AddToWishlistValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.data?.let {
                            callbacks.onAddedToWiGetWishlist(it)
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToAddToWishlist()
                    }
                }
            }
        }
    }
}