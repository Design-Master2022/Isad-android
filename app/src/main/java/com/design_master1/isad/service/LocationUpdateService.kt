package com.design_master1.isad.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.design_master1.isad.R
import com.design_master1.isad.model.network.response.SendWelcomeNotificationResponse
import com.design_master1.isad.model.network.service.SendWelcomeNotificationService
import com.design_master1.isad.model.network.validator.SendWelcomeNotificationValidator
import com.design_master1.isad.model.network.validator.SendWelcomeNotificationValidatorCallbacks
import com.design_master1.isad.ui.activities.SplashActivity
import com.design_master1.isad.utils.prefs_controller.PrefsKeys
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Starts location updates on background and publish LocationUpdateEvent upon
 * each new location result.
 */
class LocationUpdateService: Service() {

    //region data
    private lateinit var mSharedPreferences: SharedPreferences
    private val UPDATE_INTERVAL_IN_MILLISECONDS = (60*1000)*2L
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    var isSending = false

    val locationCallback = object: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val currentLocation = locationResult.lastLocation
            Log.d("Locations", "${currentLocation?.latitude}, ${currentLocation?.longitude}")
            currentLocation?.let {
                if (it.accuracy <= 30){
                    if (!isSending) sendWelcomeNotification(it.latitude.toString(), it.longitude.toString())
//                    if (!isSending) sendWelcomeNotification("29.38100635480117", "47.99112702521263")
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(this.locationRequest,
            this.locationCallback, Looper.myLooper())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        prepareForegroundNotification()
        startLocationUpdates()
        return START_STICKY
    }

    private fun prepareForegroundNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "channel_id_",
                "Location Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java) as NotificationManager
            manager.createNotificationChannel(serviceChannel);
        }
        val notificationIntent = Intent(this, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
        1,
        notificationIntent,  PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, "channel_id_")
            .setContentTitle("Location")
            .setContentText("Location Updates...")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build();
        startForeground(4, notification)
    }


    private fun initData() {
        locationRequest = LocationRequest.create()
        locationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mSharedPreferences = this.getSharedPreferences("swaac_elso_prefs_controller", Context.MODE_PRIVATE)

        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onCreate() {
        super.onCreate()
        initData()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    data class SendWelcomeNotificationParams(
        val fcmId: String,
        val lat: String,
        val lng: String
    )
    fun sendWelcomeNotification(latitude: String, longitude: String){
        isSending = true
        createRetrofit(SendWelcomeNotificationService::class.java).sendNotification(
            params = SendWelcomeNotificationParams(
                fcmId = mSharedPreferences.getString(PrefsKeys.FCM_TOKEN.name,null)?:"",
                lat = latitude,
                lng = longitude
            )
        ).enqueue(object: Callback<SendWelcomeNotificationResponse> {
            override fun onResponse(
                call: Call<SendWelcomeNotificationResponse>,
                response: Response<SendWelcomeNotificationResponse>
            ) {
                SendWelcomeNotificationValidator.validate(
                    response = response,
                    callbacks = object: SendWelcomeNotificationValidatorCallbacks {
                        override fun onUnAuthorized() {}
                        override fun onFailedToSendNotification() {
                            isSending = false
                        }
                        override fun onOutOfArea() {
                            isSending = false
//                            mFusedLocationClient.removeLocationUpdates(locationCallback)
//                            this@LocationUpdateService.stopSelf()
                        }
                        override fun onResponseFailure() {
                            isSending = false
                        }
                        override fun onNotificationSent() {
                            isSending = false
                            mFusedLocationClient.removeLocationUpdates(locationCallback)
                            this@LocationUpdateService.stopSelf()
                            mSharedPreferences.edit().putBoolean(PrefsKeys.IS_WELCOME_NOTIFICATION_SENT.name, true).apply()
                        }
                    }
                )
            }

            override fun onFailure(call: Call<SendWelcomeNotificationResponse>, t: Throwable) {
                isSending = false
                t.printStackTrace()
            }
        })
    }
    private fun getRetrofitSecondaryURL(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(/* baseUrl = */ "https://app.kifmc.com/")
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
}
