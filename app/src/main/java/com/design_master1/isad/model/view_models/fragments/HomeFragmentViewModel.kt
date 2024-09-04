package com.design_master1.isad.model.view_models.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master1.isad.model.network.client.ApiClient
import com.design_master1.isad.model.network.response.GetHomeDataResponse
import com.design_master1.isad.model.network.response.GetHomeDataResponseClasses
import com.design_master1.isad.model.network.validator.GetHomeDataValidator
import com.design_master1.isad.model.network.validator.GetHomeDataValidatorCallbacks
import com.design_master1.isad.model.provider.ElectroLibAccessProvider
import com.design_master1.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(): ViewModel(){
    @Inject lateinit var  mPrefsController: PrefsController
    @Inject lateinit var mApiClient: ApiClient
    @Inject lateinit var nElectroLibAccessProvider: ElectroLibAccessProvider
    var homeData: GetHomeDataResponseClasses.Data? = null
    enum class FetchHomeDataState{
        IDLE, FAILURE, FETCHING, FETCHED, UNAUTHORIZED
    }
    val fetchHomeDataState = MutableLiveData(FetchHomeDataState.IDLE)
    fun fetchHomeData(shouldRespond: Boolean = true){
        if (shouldRespond) fetchHomeDataState.value = FetchHomeDataState.FETCHING
        mApiClient.getHomeDataService.getHomeData()
            .enqueue(object: Callback<GetHomeDataResponse>{
                override fun onResponse(
                    call: Call<GetHomeDataResponse>,
                    response: Response<GetHomeDataResponse>
                ) {
                    GetHomeDataValidator.validate(
                        response = response,
                        callbacks = object: GetHomeDataValidatorCallbacks{
                            override fun onUnAuthorized() {
                                Log.d(TAG, "onUnAuthorized: ")
                                if (shouldRespond) fetchHomeDataState.value = FetchHomeDataState.UNAUTHORIZED
                            }

                            override fun onFailedToFetchHomeData() {
                                Log.d(TAG, "onFailedToFetchHomeData: ")
                                if (shouldRespond) fetchHomeDataState.value = FetchHomeDataState.FAILURE
                            }

                            override fun onHomeDataFetched(data: GetHomeDataResponseClasses.Data) {
                                Log.d(TAG, "onHomeDataFetched: ")
                                homeData = data
                                if (shouldRespond) fetchHomeDataState.value = FetchHomeDataState.FETCHED
                            }
                        }
                    )
                }

                override fun onFailure(call: Call<GetHomeDataResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    t.printStackTrace()
                    if (shouldRespond) fetchHomeDataState.value = FetchHomeDataState.FAILURE
                }
            })
    }
    companion object{
        private const val TAG = "HomeFragmentViewModel"
    }
}