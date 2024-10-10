package com.design_master1.isad.model.view_models.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master1.isad.model.network.client.ApiClient
import com.design_master1.isad.model.network.response.FetchAllSpeakersResponse
import com.design_master1.isad.model.network.response.FetchAllSpeakersResponseClasses
import com.design_master1.isad.model.network.validator.FetchAllSpeakersValidator
import com.design_master1.isad.model.network.validator.FetchAllSpeakersValidatorCallbacks
import com.design_master1.isad.utils.prefs_controller.PrefsController
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
    var localSpeakers = mutableListOf<FetchAllSpeakersResponseClasses.Speaker>()
    var chairman = mutableListOf<FetchAllSpeakersResponseClasses.Speaker>()
    var internationalSpeakers = mutableListOf<FetchAllSpeakersResponseClasses.Speaker>()
    var type: Int = SPEAKER_TYPE_LOCAL
    fun getAllSpeakers(shouldRespond: Boolean = true, type: Int){
        if (shouldRespond) getAllSpeakersState.value = GetAllSpeakersState.FETCHING
        mApiClient.fetchAllSpeakersService.fetchAllSpeakers(
            type = type
        ).enqueue(
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
                                this@SpeakersAndModeratorsFragmentViewModel.type = type
                                if (type == SPEAKER_TYPE_LOCAL) localSpeakers = speakers.toMutableList()
                                else if (type == SPEAKER_TYPE_INTERNATIONAL) internationalSpeakers = speakers.toMutableList()
                                else chairman = speakers.toMutableList()
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
        const val CHAIRMAN = 3
        const val SPEAKER_TYPE_LOCAL = 1
        const val SPEAKER_TYPE_INTERNATIONAL = 2
    }
}