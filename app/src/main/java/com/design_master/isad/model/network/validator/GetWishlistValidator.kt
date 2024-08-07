package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.GetWishlistResponse
import com.design_master.isad.model.network.response.GetWishlistResponseClasses
import retrofit2.Response

interface GetWishlistValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToFetchWishlist()
    fun onWishlistFetched(wishlist: List<GetWishlistResponseClasses.WishlistItem>)
}

class GetWishlistValidator {
    companion object {
        fun validate(
            response: Response<GetWishlistResponse>,
            callbacks: GetWishlistValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.wishlist?.let {
                            callbacks.onWishlistFetched(it)
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToFetchWishlist()
                    }
                }
            }
        }
    }
}