package com.design_master1.isad.model.view_models.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master1.isad.model.network.client.ApiClient
import com.design_master1.isad.model.network.response.GetAllWorkShopsResponse
import com.design_master1.isad.model.network.response.GetAllWorkShopsResponseClasses
import com.design_master1.isad.model.network.validator.GetAllWorkShopsValidator
import com.design_master1.isad.model.network.validator.GetAllWorkShopsValidatorCallbacks
import com.design_master1.isad.utils.magician.Magician
import com.design_master1.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WorkShopsFragmentViewModel @Inject constructor(): ViewModel(){
    @Inject lateinit var  mPrefsController: PrefsController
    @Inject lateinit var mApiClient: ApiClient
    @Inject lateinit var mMagician: Magician

    enum class GetAllWorkShopsState{
        IDLE, FAILURE, UNAUTHORIZED, FETCHING, FETCHED
    }
    val getAllWorkShopsState = MutableLiveData(GetAllWorkShopsState.IDLE)
    var workShopData: GetAllWorkShopsResponseClasses.Data? = null
    data class WorkShopParams(
        val deviceId: String
    )
    fun getAllWorkShops(shouldRespond: Boolean = true){
        if (shouldRespond) getAllWorkShopsState.value = GetAllWorkShopsState.FETCHING
        mApiClient.getAllWorkShopsService.getAllShops(
            populate = true,
            params = WorkShopParams(deviceId = mPrefsController.getUserUid()!!)
        ).enqueue(object: Callback<GetAllWorkShopsResponse>{
                override fun onResponse(
                    call: Call<GetAllWorkShopsResponse>,
                    response: Response<GetAllWorkShopsResponse>
                ) {
                    GetAllWorkShopsValidator.validate(
                        response = response,
                        callbacks = object: GetAllWorkShopsValidatorCallbacks{
                            override fun onUnAuthorized() {
                                Log.d(TAG, "onUnAuthorized: ")
                                if (shouldRespond) getAllWorkShopsState.value = GetAllWorkShopsState.UNAUTHORIZED

                            }
                            override fun onFailedToFetchWorkShops() {
                                Log.d(TAG, "onFailedToFetchWorkShops: ")
                                if (shouldRespond) getAllWorkShopsState.value = GetAllWorkShopsState.FAILURE
                            }
                            override fun onWorkShopsFetched(workShopData: GetAllWorkShopsResponseClasses.Data) {
                                Log.d(TAG, "onWorkShopsFetched: ")
                                this@WorkShopsFragmentViewModel.workShopData = workShopData
                                getAllWorkShopsState.value = GetAllWorkShopsState.FETCHED
                            }
                        }
                    )
                }

                override fun onFailure(call: Call<GetAllWorkShopsResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    t.printStackTrace()
                    if (shouldRespond) getAllWorkShopsState.value = GetAllWorkShopsState.FAILURE
                }

            })
    }
    companion object{
        private const val TAG = "NotificationsFragmentVi"
    }
}