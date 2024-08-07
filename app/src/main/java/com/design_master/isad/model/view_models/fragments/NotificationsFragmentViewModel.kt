package com.design_master.isad.model.view_models.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master.isad.model.network.client.ApiClient
import com.design_master.isad.model.network.response.GetEnabledNotificationsResponse
import com.design_master.isad.model.network.response.GetEnabledNotificationsResponseClasses
import com.design_master.isad.model.network.validator.GetNotificationValidator
import com.design_master.isad.model.network.validator.GetNotificationValidatorCallbacks
import com.design_master.isad.utils.magician.Magician
import com.design_master.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NotificationsFragmentViewModel @Inject constructor(): ViewModel(){
    @Inject lateinit var  mPrefsController: PrefsController
    @Inject lateinit var mApiClient: ApiClient
    @Inject lateinit var mMagician: Magician

    enum class GetAllNotificationsState{
        IDLE, FAILURE, UNAUTHORIZED, FETCHING, FETCHED
    }
    val getAllNotificationsState = MutableLiveData(GetAllNotificationsState.IDLE)
    var notifications = mutableListOf<GetEnabledNotificationsResponseClasses.Notification>()
    fun getAllNotifications(shouldRespond: Boolean = true){
        if (shouldRespond) getAllNotificationsState.value = GetAllNotificationsState.FETCHING
        mApiClient.getNotificationsService.getAllNotifications(
            populate = true,
            deviceId = mPrefsController.getUserUid()!!
        ).enqueue(object: Callback<GetEnabledNotificationsResponse>{
                override fun onResponse(
                    call: Call<GetEnabledNotificationsResponse>,
                    response: Response<GetEnabledNotificationsResponse>
                ) {
                    GetNotificationValidator.validate(
                        response = response,
                        callbacks = object: GetNotificationValidatorCallbacks{
                            override fun onUnAuthorized() {
                                Log.d(TAG, "onUnAuthorized: ")
                                if (shouldRespond) getAllNotificationsState.value = GetAllNotificationsState.UNAUTHORIZED
                            }
                            override fun onFailedToFetchNotifications() {
                                Log.d(TAG, "onFailedToFetchNotifications: ")
                                if (shouldRespond) getAllNotificationsState.value = GetAllNotificationsState.FAILURE
                            }
                            override fun onNotificationsFetched(notifications: List<GetEnabledNotificationsResponseClasses.Notification>) {
                                Log.d(TAG, "onNotificationsFetched: ")
                                this@NotificationsFragmentViewModel.notifications = notifications.toMutableList()
                                getAllNotificationsState.value = GetAllNotificationsState.FETCHED
                            }
                        }
                    )
                }

                override fun onFailure(call: Call<GetEnabledNotificationsResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    t.printStackTrace()
                    if (shouldRespond) getAllNotificationsState.value = GetAllNotificationsState.FAILURE
                }
            })
    }
    companion object{
        private const val TAG = "NotificationsFragmentVi"
    }
}