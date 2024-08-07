package com.design_master.isad.model.view_models.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master.isad.model.network.client.ApiClient
import com.design_master.isad.model.network.response.FetchAllSpeakersResponse
import com.design_master.isad.model.network.response.FetchAllSpeakersResponseClasses
import com.design_master.isad.model.network.validator.FetchAllSpeakersValidator
import com.design_master.isad.model.network.validator.FetchAllSpeakersValidatorCallbacks
import com.design_master.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SpeakersAndModeratorsFragmentViewModel @Inject constructor(): ViewModel(){
    @Inject lateinit var  mPrefsController: PrefsController
    @Inject lateinit var mApiClient: ApiClient

    enum class GetAllSpeakersState{
        IDLE, FAILURE, UNAUTHORIZED, FETCHING, FETCHED
    }
    val getAllSpeakersState = MutableLiveData(GetAllSpeakersState.IDLE)
    var speakers = mutableListOf<FetchAllSpeakersResponseClasses.Speaker>()
    fun getAllSpeakers(shouldRespond: Boolean = true){
        if (shouldRespond) getAllSpeakersState.value = GetAllSpeakersState.FETCHING
        mApiClient.fetchAllSpeakersService.fetchAllSpeakers().enqueue(
            object: Callback<FetchAllSpeakersResponse>{
                override fun onResponse(
                    call: Call<FetchAllSpeakersResponse>,
                    response: Response<FetchAllSpeakersResponse>
                ) {
                    FetchAllSpeakersValidator.validate(
                        response = response,
                        callbacks = object: FetchAllSpeakersValidatorCallbacks{
                            override fun onUnAuthorized() {
                                Log.d(TAG, "onUnAuthorized: ")
                                if (shouldRespond) getAllSpeakersState.value = GetAllSpeakersState.UNAUTHORIZED

                            }

                            override fun onFailedToFetchSpeakers() {
                                Log.d(TAG, "onFailedToFetchSpeakers: ")
                                if (shouldRespond) getAllSpeakersState.value = GetAllSpeakersState.FAILURE
                            }

                            override fun onSpeakersFetched(speakers: List<FetchAllSpeakersResponseClasses.Speaker>) {
                                Log.d(TAG, "onSpeakersFetched: ")
                                this@SpeakersAndModeratorsFragmentViewModel.speakers = speakers.toMutableList()
                                getAllSpeakersState.value = GetAllSpeakersState.FETCHED
                            }
                        }
                    )
                }

                override fun onFailure(call: Call<FetchAllSpeakersResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    t.printStackTrace()
                    getAllSpeakersState.value = GetAllSpeakersState.FAILURE
                }

            })
    }
    companion object{
        private const val TAG = "NotificationsFragmentVi"
    }
}