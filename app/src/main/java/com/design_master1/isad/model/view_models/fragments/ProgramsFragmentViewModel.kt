package com.design_master1.isad.model.view_models.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master1.isad.model.network.client.ApiClient
import com.design_master1.isad.model.network.response.GetAllScientificProgramsResponse
import com.design_master1.isad.model.network.response.GetAllScientificProgramsResponseClasses
import com.design_master1.isad.model.network.validator.GetAllScientificProgramsValidator
import com.design_master1.isad.model.network.validator.GetAllScientificProgramsValidatorCallbacks
import com.design_master1.isad.utils.magician.Magician
import com.design_master1.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProgramsFragmentViewModel @Inject constructor(): ViewModel(){
    @Inject lateinit var  mPrefsController: PrefsController
    @Inject lateinit var mApiClient: ApiClient
    @Inject lateinit var mMagician: Magician

    enum class GetAllScientificProgramsState{
        IDLE, FAILURE, UNAUTHORIZED, FETCHING, FETCHED
    }
    val getAllScientificProgramsState = MutableLiveData(GetAllScientificProgramsState.IDLE)
    var programsData: GetAllScientificProgramsResponseClasses.Data? = null
    data class ProgramParams(
        val deviceId: String
    )
    fun getAllPrograms(shouldRespond: Boolean = true){
        if (shouldRespond) getAllScientificProgramsState.value = GetAllScientificProgramsState.FETCHING
        mApiClient.getAllScientificProgramsService.getAllScientificPrograms(
            populate = true,
            params = ProgramParams(deviceId = mPrefsController.getUserUid()!!)
        ).enqueue(object: Callback<GetAllScientificProgramsResponse>{
                override fun onResponse(
                    call: Call<GetAllScientificProgramsResponse>,
                    response: Response<GetAllScientificProgramsResponse>
                ) {
                    GetAllScientificProgramsValidator.validate(
                        response = response,
                        callbacks = object: GetAllScientificProgramsValidatorCallbacks{
                            override fun onUnAuthorized() {
                                Log.d(TAG, "onUnAuthorized: ")
                                if (shouldRespond) getAllScientificProgramsState.value = GetAllScientificProgramsState.UNAUTHORIZED

                            }
                            override fun onFailedToFetchScientificPrograms() {
                                Log.d(TAG, "onFailedToFetchScientificPrograms: ")
                                if (shouldRespond) getAllScientificProgramsState.value = GetAllScientificProgramsState.FAILURE
                            }
                            override fun onScientificProgramsFetched(programsData: GetAllScientificProgramsResponseClasses.Data) {
                                Log.d(TAG, "onScientificProgramsFetched: ")
                                this@ProgramsFragmentViewModel.programsData = programsData
                                getAllScientificProgramsState.value = GetAllScientificProgramsState.FETCHED
                            }
                        }
                    )
                }
                override fun onFailure(call: Call<GetAllScientificProgramsResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    t.printStackTrace()
                    if (shouldRespond) getAllScientificProgramsState.value = GetAllScientificProgramsState.FAILURE
                }
            })
    }
    companion object{
        private const val TAG = "NotificationsFragmentVi"
    }
}