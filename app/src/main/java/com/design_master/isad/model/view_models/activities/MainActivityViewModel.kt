package com.design_master.isad.model.view_models.activities

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master.isad.model.listeners.AddToWishlistListener
import com.design_master.isad.model.listeners.DisableNotificationListener
import com.design_master.isad.model.listeners.EnableNotificationListener
import com.design_master.isad.model.listeners.RemoveFromWishlistListener
import com.design_master.isad.model.network.client.ApiClient
import com.design_master.isad.model.network.response.AddToWishlistResponse
import com.design_master.isad.model.network.response.AddToWishlistResponseClasses
import com.design_master.isad.model.network.response.DisableNotificationResponse
import com.design_master.isad.model.network.response.EnableNotificationResponse
import com.design_master.isad.model.network.response.EnableNotificationResponseClasses
import com.design_master.isad.model.network.response.FetchMenuResponse
import com.design_master.isad.model.network.response.FetchMenuResponseClasses
import com.design_master.isad.model.network.response.GetAllScientificProgramsResponseClasses
import com.design_master.isad.model.network.response.GetAllWorkShopsResponseClasses
import com.design_master.isad.model.network.response.GetWishlistResponseClasses
import com.design_master.isad.model.network.response.RemoveFromWishlistResponse
import com.design_master.isad.model.network.validator.AddToWishlistValidator
import com.design_master.isad.model.network.validator.AddToWishlistValidatorCallbacks
import com.design_master.isad.model.network.validator.DisableNotificationValidator
import com.design_master.isad.model.network.validator.DisableNotificationValidatorCallbacks
import com.design_master.isad.model.network.validator.EnableNotificationValidator
import com.design_master.isad.model.network.validator.EnableNotificationValidatorCallbacks
import com.design_master.isad.model.network.validator.FetchMenuValidator
import com.design_master.isad.model.network.validator.FetchMenuValidatorCallbacks
import com.design_master.isad.model.network.validator.RemoveFromWishlistValidator
import com.design_master.isad.model.network.validator.RemoveFromWishlistValidatorCallbacks
import com.design_master.isad.model.provider.ElectroLibAccessProvider
import com.design_master.isad.utils.helper.Helper
import com.design_master.isad.utils.helper.PermissionHelper
import com.design_master.isad.utils.magician.Magician
import com.design_master.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): ViewModel() {
    @Inject lateinit var mPrefsController: PrefsController
    @Inject lateinit var mElectroLibAccessProvider: ElectroLibAccessProvider
    @Inject lateinit var  mApiClient: ApiClient
    @Inject lateinit var mMagician: Magician
    @Inject lateinit var mHelper: Helper
    @Inject lateinit var mPermissionHelper: PermissionHelper


    /**
     * LoginWithOtpFragment Params
     * */
    var isForQrCodeFragment = false

    /**
     *  TabsFragment Values
     * */
    var cmeUrl = ""
    var videoUrl = ""

    /**
     *  Selected Program and WorkShop for AddNoteFragment
     * */
    var programOrWorkShopId: String? = null
    var mSelectedWorkShop: GetAllWorkShopsResponseClasses.WorkShop? = null
    var mSelectedProgram: GetAllScientificProgramsResponseClasses.ScientificProgram? = null
    var mSelectedWishlistItem: GetWishlistResponseClasses.WishlistItem? = null

    data class DeviceIdParam(
        val deviceId: String
    )
    data class AddToWishlistParams(
        val deviceId: String,
        val programOrWorkshopId: String,
        val type: String,
        val dayWiseId: String
    )
    fun addToWishlist(
        type: String,
        dayWiseId: String,
        programOrWorkShopId: String,
        listener: AddToWishlistListener
    ){
        mApiClient.addToWishlistService.addToWishlist(
            params = AddToWishlistParams(
                deviceId = mPrefsController.getUserUid()!!,
                programOrWorkshopId = programOrWorkShopId,
                type = type,
                dayWiseId = dayWiseId
            )
        ).enqueue(object: Callback<AddToWishlistResponse>{
            override fun onResponse(
                call: Call<AddToWishlistResponse>,
                response: Response<AddToWishlistResponse>
            ) {
                AddToWishlistValidator.validate(
                    response = response,
                    callbacks = object: AddToWishlistValidatorCallbacks{
                        override fun onUnAuthorized() {
                            Log.d(TAG, "onUnAuthorized: ")
                            listener.onFailedToAdd()
                        }

                        override fun onFailedToAddToWishlist() {
                            Log.d(TAG, "onFailedToAddToWishlist: ")
                            listener.onFailedToAdd()
                        }

                        override fun onAddedToWiGetWishlist(data: AddToWishlistResponseClasses.Data) {
                            Log.d(TAG, "onAddedToWiGetWishlist: ")
                            listener.onAddedToWishlist()
                        }
                    }
                )
            }

            override fun onFailure(call: Call<AddToWishlistResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                t.printStackTrace()
                listener.onFailedToAdd()
            }
        })
    }
    fun removeFromWishlist(
        dayWiseId: String,
        listener: RemoveFromWishlistListener
    ){
        mApiClient.removeFromWishlistService.removeFromWishlist(
            dayWiseId = dayWiseId,
            deviceId = DeviceIdParam(
                deviceId = mPrefsController.getUserUid()!!,
            )
        ).enqueue(object: Callback<RemoveFromWishlistResponse>{
            override fun onResponse(
                call: Call<RemoveFromWishlistResponse>,
                response: Response<RemoveFromWishlistResponse>
            ) {
                RemoveFromWishlistValidator.validate(
                    response = response,
                    callbacks = object: RemoveFromWishlistValidatorCallbacks{
                        override fun onUnAuthorized() {
                            Log.d(TAG, "onUnAuthorized: ")
                            listener.onFailedToRemove()
                        }

                        override fun onFailedToRemoveFromWishlist() {
                            listener.onFailedToRemove()
                            Log.d(TAG, "onFailedToRemoveFromWishlist: ")
                        }

                        override fun onRemovedFromWishlist() {
                            listener.onRemovedFromWishlist()
                            Log.d(TAG, "onRemovedFromWishlist: ")
                        }
                    }
                )
            }

            override fun onFailure(call: Call<RemoveFromWishlistResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                t.printStackTrace()
                listener.onFailedToRemove()
            }
        })
    }

    var menuData = MutableLiveData<MutableList<FetchMenuResponseClasses.MenuItem>>()

    fun fetchMenu(){
        mApiClient.fetchMenuService.fetch()
            .enqueue(object: Callback<FetchMenuResponse>{
            override fun onResponse(
                call: Call<FetchMenuResponse>,
                response: Response<FetchMenuResponse>
            ) {
                FetchMenuValidator.validate(
                    response = response,
                    callbacks = object: FetchMenuValidatorCallbacks{
                        override fun onUnAuthorized() {
                            Log.d(TAG, "onUnAuthorized: ")
                            fetchMenu()
                        }
                        override fun onFailedToFetchMenu() {
                            Log.d(TAG, "onFailedToFetchMenu: ")
                            fetchMenu()
                        }
                        override fun onMenuFetched(data: List<FetchMenuResponseClasses.MenuItem>) {
                            Log.d(TAG, "onMenuFetched: ")
                            menuData.value = data.toMutableList()
                        }
                    }
                )
            }
            override fun onFailure(call: Call<FetchMenuResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                t.printStackTrace()
                fetchMenu()
            }
        })
    }

    data class EnableNotificationParams(
        val deviceId: String,
        val programOrWorkshopId: String,
        val type: String,
        val dayWiseId: String
    )
    fun enableNotification(
        type: String,
        dayWiseId: String,
        programOrWorkShopId: String,
        listener: EnableNotificationListener
    ){
        mApiClient.enableNotificationService.enableNotification(
            params = EnableNotificationParams(
                deviceId = mPrefsController.getUserUid()!!,
                programOrWorkshopId = programOrWorkShopId,
                type = type,
                dayWiseId = dayWiseId
            )
        ).enqueue(object: Callback<EnableNotificationResponse>{
            override fun onResponse(
                call: Call<EnableNotificationResponse>,
                response: Response<EnableNotificationResponse>
            ) {
                EnableNotificationValidator.validate(
                    response = response,
                    callbacks = object: EnableNotificationValidatorCallbacks{
                        override fun onUnAuthorized() {
                            Log.d(TAG, "onUnAuthorized: ")
                            listener.onFailedToEnableNotification()
                        }

                        override fun onFailedToEnableNotification() {
                            Log.d(TAG, "onFailedToEnableNotification: ")
                            listener.onFailedToEnableNotification()
                        }

                        override fun onNotificationEnabled(data: EnableNotificationResponseClasses.Data) {
                            Log.d(TAG, "onNotificationEnabled: ")
                            listener.onNotificationEnabled(data)
                        }
                    }
                )
            }

            override fun onFailure(call: Call<EnableNotificationResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                t.printStackTrace()
                listener.onFailedToEnableNotification()
            }
        })
    }
    fun disableNotification(
        dayWiseId: String,
        listener: DisableNotificationListener
    ){
        mApiClient.disableNotificationService.disableNotification(
            id = dayWiseId,
            deviceId = DeviceIdParam(
                deviceId = mPrefsController.getUserUid()!!
            )
        ).enqueue(object: Callback<DisableNotificationResponse>{
            override fun onResponse(
                call: Call<DisableNotificationResponse>,
                response: Response<DisableNotificationResponse>
            ) {
                DisableNotificationValidator.validate(
                    response = response,
                    callbacks = object: DisableNotificationValidatorCallbacks{
                        override fun onUnAuthorized() {
                            Log.d(TAG, "onUnAuthorized: ")
                            listener.onFailedToDisableNotification()
                        }

                        override fun onFailedToDisableNotification() {
                            Log.d(TAG, "onFailedToDisableNotification: ")
                            listener.onFailedToDisableNotification()
                        }

                        override fun onNotificationDisabled() {
                            Log.d(TAG, "onNotificationDisabled: ")
                            listener.onNotificationDisabled()
                        }
                    }
                )
            }

            override fun onFailure(call: Call<DisableNotificationResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                t.printStackTrace()
                listener.onFailedToDisableNotification()
            }
        })
    }
    companion object {
        private const val TAG = "SplashActivityViewModel"
    }

}