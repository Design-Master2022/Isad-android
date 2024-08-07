package com.design_master.isad.model.view_models.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master.isad.model.network.client.ApiClient
import com.design_master.isad.model.network.response.AddFeedbackResponse
import com.design_master.isad.model.network.service.AddFeedbackParams
import com.design_master.isad.model.network.validator.AddFeedbackValidator
import com.design_master.isad.model.network.validator.AddFeedbackValidatorCallbacks
import com.design_master.isad.utils.magician.Magician
import com.design_master.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FeedbackFragmentViewModel @Inject constructor(): ViewModel(){
    @Inject lateinit var  mPrefsController: PrefsController
    @Inject lateinit var mApiClient: ApiClient
    @Inject lateinit var mMagician: Magician

    enum class AddFeedbackState{
        IDLE, FAILURE, UNAUTHORIZED, ADDING, ADDED
    }
    val addFeedbackState = MutableLiveData(AddFeedbackState.IDLE)
    fun addFeedback(
        name: String,
        phone: String,
        email: String,
        message: String
    ){
        addFeedbackState.value = AddFeedbackState.ADDING
        mApiClient.addFeedbackService.add(
            params = AddFeedbackParams(
                name = name,
                phone = phone,
                email = email,
                comment = message,
                deviceId = mPrefsController.getUserUid()!!
            )
        ).enqueue(object: Callback<AddFeedbackResponse>{
                override fun onResponse(
                    call: Call<AddFeedbackResponse>,
                    response: Response<AddFeedbackResponse>
                ) {
                    AddFeedbackValidator.validate(
                        response = response,
                        callbacks = object: AddFeedbackValidatorCallbacks{
                            override fun onUnAuthorized() {
                                Log.d(TAG, "onUnAuthorized: ")
                                addFeedbackState.value = AddFeedbackState.UNAUTHORIZED
                            }
                            override fun onFailedToAddFeedback() {
                               Log.d(TAG, "onFailedToAddFeedback: ")
                                addFeedbackState.value = AddFeedbackState.FAILURE
                            }
                            override fun onFeedbackAdded() {
                                Log.d(TAG, "onFailedToAddFeedback: ")
                                addFeedbackState.value = AddFeedbackState.ADDED
                            }
                        }
                    )
                }
                override fun onFailure(call: Call<AddFeedbackResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    t.printStackTrace()
                    addFeedbackState.value = AddFeedbackState.FAILURE
                }
            })
    }
    companion object{
        private const val TAG = "FeedbackFragmentViewMod"
    }
}