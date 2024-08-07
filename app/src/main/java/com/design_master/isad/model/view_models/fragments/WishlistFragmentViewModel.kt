package com.design_master.isad.model.view_models.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master.isad.model.network.client.ApiClient
import com.design_master.isad.model.network.response.GetWishlistResponse
import com.design_master.isad.model.network.response.GetWishlistResponseClasses
import com.design_master.isad.model.network.validator.GetWishlistValidator
import com.design_master.isad.model.network.validator.GetWishlistValidatorCallbacks
import com.design_master.isad.utils.magician.Magician
import com.design_master.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WishlistFragmentViewModel @Inject constructor(): ViewModel(){
    @Inject lateinit var  mPrefsController: PrefsController
    @Inject lateinit var mApiClient: ApiClient
    @Inject lateinit var mMagician: Magician

    enum class GetWishlistState{
        IDLE, FAILURE, UNAUTHORIZED, FETCHING, FETCHED
    }
    val getWishlistState = MutableLiveData(GetWishlistState.IDLE)
    var wishlist = mutableListOf<GetWishlistResponseClasses.WishlistItem>()
    fun getWishlist(shouldRespond: Boolean = true){
        if (shouldRespond) getWishlistState.value = GetWishlistState.FETCHING
        mApiClient.getWishlistService.getWishlist(
            deviceId = mPrefsController.getUserUid()!!,
            populate = true
        ).enqueue(object: Callback<GetWishlistResponse>{
                override fun onResponse(
                    call: Call<GetWishlistResponse>,
                    response: Response<GetWishlistResponse>
                ) {
                    GetWishlistValidator.validate(
                        response = response,
                        callbacks = object: GetWishlistValidatorCallbacks{
                            override fun onUnAuthorized() {
                                Log.d(TAG, "onUnAuthorized: ")
                                if (shouldRespond) getWishlistState.value = GetWishlistState.UNAUTHORIZED
                            }
                            override fun onFailedToFetchWishlist() {
                                Log.d(TAG, "onFailedToFetchWishlist: ")
                                if (shouldRespond) getWishlistState.value = GetWishlistState.FAILURE
                            }
                            override fun onWishlistFetched(wishlist: List<GetWishlistResponseClasses.WishlistItem>) {
                                Log.d(TAG, "onWishlistFetched: ")
                                this@WishlistFragmentViewModel.wishlist = wishlist.toMutableList()
                                getWishlistState.value = GetWishlistState.FETCHED
                            }
                        }
                    )
                }
                override fun onFailure(call: Call<GetWishlistResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    t.printStackTrace()
                    if (shouldRespond) getWishlistState.value = GetWishlistState.FAILURE
                }

            })
    }
    companion object{
        private const val TAG = "NotificationsFragmentVi"
    }
}