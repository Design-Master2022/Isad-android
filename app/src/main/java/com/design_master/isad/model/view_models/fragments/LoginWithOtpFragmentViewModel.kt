package com.design_master.isad.model.view_models.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master.isad.model.network.client.ApiClient
import com.design_master.isad.model.network.response.LoginWithOtpResponse
import com.design_master.isad.model.network.response.VerifyEmailForQRCodeResponse
import com.design_master.isad.model.network.response.VerifyOtpResponse
import com.design_master.isad.model.network.service.LoginWithOtpService
import com.design_master.isad.model.network.service.VerifyEmailForQRCodeService
import com.design_master.isad.model.network.service.VerifyOtpService
import com.design_master.isad.model.network.validator.LoginWithOtpValidator
import com.design_master.isad.model.network.validator.LoginWithOtpValidatorCallbacks
import com.design_master.isad.model.network.validator.VerifyEmailForQRCodeValidator
import com.design_master.isad.model.network.validator.VerifyEmailForQRCodeValidatorCallbacks
import com.design_master.isad.model.network.validator.VerifyOtpValidator
import com.design_master.isad.model.network.validator.VerifyOtpValidatorCallbacks
import com.design_master.isad.model.provider.ElectroLibAccessProvider
import com.design_master.isad.utils.helper.Helper
import com.design_master.isad.utils.magician.Magician
import com.design_master.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LoginWithOtpFragmentViewModel @Inject constructor(): ViewModel(){
    @Inject lateinit var  mPrefsController: PrefsController
    @Inject lateinit var mMagician: Magician
    @Inject lateinit var mApiClient: ApiClient
    @Inject lateinit var mHelper: Helper
    @Inject lateinit var electroLibAccessProvider: ElectroLibAccessProvider

    enum class LoginWithOtpState{
        IDLE, FAILURE, SENDING_OTP, OTP_SENT, UNAUTHORIZED
    }
    val loginWithOtpState = MutableLiveData(LoginWithOtpState.IDLE)
    fun loginWithOtp(email: String){
        loginWithOtpState.value = LoginWithOtpState.SENDING_OTP
        createRetrofit(LoginWithOtpService::class.java).loginWithOtp(
            email = email
        ).enqueue(object: Callback<LoginWithOtpResponse>{
                override fun onResponse(
                    call: Call<LoginWithOtpResponse>,
                    response: Response<LoginWithOtpResponse>
                ) {
                    LoginWithOtpValidator.validate(
                        response = response,
                        callbacks = object: LoginWithOtpValidatorCallbacks{
                            override fun onUnAuthorized() {
                                Log.d(TAG, "onUnAuthorized: ")
                                loginWithOtpState.value = LoginWithOtpState.UNAUTHORIZED
                                loginWithOtpState.value = LoginWithOtpState.IDLE
                            }
                            override fun onFailedToSendOtp() {
                                Log.d(TAG, "onFailedToSendOtp: ")
                                loginWithOtpState.value = LoginWithOtpState.FAILURE
                                loginWithOtpState.value = LoginWithOtpState.IDLE
                            }
                            override fun onOtpSent() {
                                Log.d(TAG, "onOtpSent: ")
                                loginWithOtpState.value = LoginWithOtpState.OTP_SENT
                                loginWithOtpState.value = LoginWithOtpState.IDLE
                            }
                        }
                    )
                }

                override fun onFailure(call: Call<LoginWithOtpResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    t.printStackTrace()
                    loginWithOtpState.value = LoginWithOtpState.FAILURE
                    loginWithOtpState.value = LoginWithOtpState.IDLE
                }
            })
    }

    enum class VerifyEmailForQRCodeState{
        IDLE, FAILURE, VERIFYING, VERIFIED, UNAUTHORIZED
    }
    var registrationCode = ""
    val verifyEmailForQRCodeState = MutableLiveData(VerifyEmailForQRCodeState.IDLE)
    fun verifyEmailForQRCode(email: String){
        verifyEmailForQRCodeState.value = VerifyEmailForQRCodeState.VERIFYING
        createRetrofit(VerifyEmailForQRCodeService::class.java).verify(
            email = email
        ).enqueue(object: Callback<VerifyEmailForQRCodeResponse>{
            override fun onResponse(
                call: Call<VerifyEmailForQRCodeResponse>,
                response: Response<VerifyEmailForQRCodeResponse>
            ) {
                VerifyEmailForQRCodeValidator.validate(
                    response = response,
                    callbacks = object: VerifyEmailForQRCodeValidatorCallbacks {
                        override fun onUnAuthorized() {
                            Log.d(TAG, "onUnAuthorized: ")
                            verifyEmailForQRCodeState.value = VerifyEmailForQRCodeState.UNAUTHORIZED
                            verifyEmailForQRCodeState.value = VerifyEmailForQRCodeState.IDLE
                        }
                        override fun onFailedToVerify() {
                            Log.d(TAG, "onFailedToVerify: ")
                            verifyEmailForQRCodeState.value = VerifyEmailForQRCodeState.FAILURE
                            verifyEmailForQRCodeState.value = VerifyEmailForQRCodeState.IDLE
                        }
                        override fun onEmailVerified(registrationCode: String) {
                            Log.d(TAG, "onEmailVerified: ")
                            this@LoginWithOtpFragmentViewModel.registrationCode = registrationCode
                            verifyEmailForQRCodeState.value = VerifyEmailForQRCodeState.VERIFIED
                            verifyEmailForQRCodeState.value = VerifyEmailForQRCodeState.IDLE
                        }
                    }
                )
            }
            override fun onFailure(call: Call<VerifyEmailForQRCodeResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                t.printStackTrace()
                verifyEmailForQRCodeState.value = VerifyEmailForQRCodeState.FAILURE
                verifyEmailForQRCodeState.value = VerifyEmailForQRCodeState.IDLE
            }
        })
    }

    enum class OtpVerifyState{
        IDLE, FAILURE, VERIFYING, VERIFIED, UNAUTHORIZED, WRONG_OTP
    }
    var cmeUrl = ""
    var videoUrl = ""
    val otpVerifyState = MutableLiveData(OtpVerifyState.IDLE)
    fun verifyOtp(email: String, otp: String){
        otpVerifyState.value = OtpVerifyState.VERIFYING
        createRetrofit(VerifyOtpService::class.java).verify(
            email = email,
            otp = otp
        ).enqueue(object: Callback<VerifyOtpResponse>{
            override fun onResponse(
                call: Call<VerifyOtpResponse>,
                response: Response<VerifyOtpResponse>
            ) {
                VerifyOtpValidator.validate(
                    response = response,
                    callbacks = object: VerifyOtpValidatorCallbacks{
                        override fun onUnAuthorized() {
                            Log.d(TAG, "onUnAuthorized: ")
                            otpVerifyState.value = OtpVerifyState.UNAUTHORIZED
                        }

                        override fun onFailedToVerifyOtp() {
                            Log.d(TAG, "onFailedToVerifyOtp: ")
                            otpVerifyState.value = OtpVerifyState.FAILURE
                        }

                        override fun onWrongOtp() {
                            Log.d(TAG, "onWrongOtp: ")
                            otpVerifyState.value = OtpVerifyState.WRONG_OTP
                        }

                        override fun onOtpVerified(cmeUrl: String, videoUrl: String) {
                            Log.d(TAG, "onOtpVerified: ")
                            this@LoginWithOtpFragmentViewModel.cmeUrl = cmeUrl
                            this@LoginWithOtpFragmentViewModel.videoUrl = videoUrl

                            otpVerifyState.value = OtpVerifyState.VERIFIED
                        }
                    }
                )
            }

            override fun onFailure(call: Call<VerifyOtpResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                t.printStackTrace()
                otpVerifyState.value = OtpVerifyState.FAILURE
            }
        })
    }
    private fun getRetrofitSecondaryURL(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(/* baseUrl = */ " https://kifmc.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
    }
    private fun <T: Any> createRetrofit(service: Class<T>): T{
        return getRetrofitSecondaryURL().create(service)
    }
    private fun getOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(logging)
            .build()
    }
    companion object{
        private const val TAG = "AddNoteFragmentViewMode"
    }
}