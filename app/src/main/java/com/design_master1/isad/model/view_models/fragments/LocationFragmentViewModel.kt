package com.design_master1.isad.model.view_models.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master1.isad.model.network.client.ApiClient
import com.design_master1.isad.model.network.response.FetchLocationResponse
import com.design_master1.isad.model.network.response.FetchLocationResponseClasses
import com.design_master1.isad.model.network.validator.FetchLocationValidator
import com.design_master1.isad.model.network.validator.FetchLocationValidatorCallbacks
import com.design_master1.isad.model.provider.ElectroLibAccessProvider
import com.design_master1.isad.utils.magician.Magician
import com.design_master1.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LocationFragmentViewModel @Inject constructor(): ViewModel(){
    @Inject lateinit var  mPrefsController: PrefsController
    @Inject lateinit var mApiClient: ApiClient
    @Inject lateinit var mMagician: Magician
    @Inject lateinit var mElectroLibAccessProvider: ElectroLibAccessProvider

    enum class FetchLocationState{
        IDLE, FAILURE, FETCHING, FETCHED, UNAUTHORIZED
    }
    var locationData: FetchLocationResponseClasses.Data? = null
    var images = mutableListOf<FetchLocationResponseClasses.Image>()
    val fetchLocationState = MutableLiveData(FetchLocationState.IDLE)
    fun fetchLocation(){
        fetchLocationState.value = FetchLocationState.FETCHING
        mApiClient.fetchLocationService.fetch().enqueue(
            object: Callback<FetchLocationResponse>{
                override fun onResponse(
                    call: Call<FetchLocationResponse>,
                    response: Response<FetchLocationResponse>
                ) {
                    FetchLocationValidator.validate(
                        response = response,
                        callbacks = object: FetchLocationValidatorCallbacks{
                            override fun onUnAuthorized() {
                                Log.d(TAG, "onUnAuthorized: ")
                                fetchLocationState.value = FetchLocationState.UNAUTHORIZED
                            }
                            override fun onFailedToFetchLocation() {
                                Log.d(TAG, "onFailedToFetchLocation: ")
                                fetchLocationState.value = FetchLocationState.FAILURE
                            }

                            override fun onLocationFetched(
                                data: FetchLocationResponseClasses.Data,
                                images: List<FetchLocationResponseClasses.Image>
                            ) {
                                Log.d(TAG, "onLocationFetched: ")
                                locationData = data
                                this@LocationFragmentViewModel.images = images.toMutableList()
                                fetchLocationState.value = FetchLocationState.FETCHED
                            }
                        }
                    )
                }
                override fun onFailure(call: Call<FetchLocationResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    t.printStackTrace()
                    fetchLocationState.value = FetchLocationState.FAILURE
                }
            })
    }
    companion object{
        private const val TAG = "AddNoteFragmentViewMode"
    }
}