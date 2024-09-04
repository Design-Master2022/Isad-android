package com.design_master1.isad.model.view_models.activities

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master1.isad.model.network.client.ApiClient
import com.design_master1.isad.model.network.response.LoginResponse
import com.design_master1.isad.model.network.validator.LoginValidator
import com.design_master1.isad.model.network.validator.LoginValidatorCallbacks
import com.design_master1.isad.utils.magician.Magician
import com.design_master1.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(): ViewModel() {

    @Inject lateinit var mPrefsController: PrefsController
    @Inject lateinit var mApiClient: ApiClient
    @Inject lateinit var mMagician: Magician

    enum class LoginState{
        IDLE, LOGGING_IN, LOGGED_IN, FAILURE, UNAUTHORIZED
    }
    val loginState = MutableLiveData(LoginState.IDLE)
    data class LoginParams(
        val deviceId: String,
        val fcmId: String
    )
    fun login(deviceId: String){
        loginState.value = LoginState.LOGGING_IN
        mApiClient.loginService.login(
            loginParams = LoginParams(deviceId, mPrefsController.getFCMToken()!!)
        ).enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                LoginValidator.validate(
                    response = response,
                    callbacks = object: LoginValidatorCallbacks {
                        override fun onUnAuthorized() {
                            Log.d(TAG, "onUnAuthorized: ")
                            loginState.value = LoginState.UNAUTHORIZED
                        }

                        override fun onFailedToLogin() {
                            Log.d(TAG, "onFailedToLogin: ")
                            loginState.value = LoginState.FAILURE
                        }

                        override fun onLoggedIn() {
                            Log.d(TAG, "onLoggedIn: ")
                            mPrefsController.saveLogin(true)
                            mPrefsController.saveUserUid(deviceId)
                            loginState.value = LoginState.LOGGED_IN
                        }
                    }
                )
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                t.printStackTrace()
                loginState.value = LoginState.FAILURE
            }
        })
    }

    companion object {
        private const val TAG = "SplashActivityViewModel"
    }

}