package com.design_master1.isad.utils.prefs_controller

import android.content.SharedPreferences
import androidx.core.content.edit

class PrefsController(private val sharedPreferences: SharedPreferences) {

    fun saveUserUid(userUid: String) {  // Device Id is User Id
        sharedPreferences.edit {
            putString(PrefsKeys.USER_UID.name, userUid)
        }
    }
    fun getUserUid(): String? {
        return sharedPreferences.getString(PrefsKeys.USER_UID.name, null)
    }

    fun saveLogin(isLoggedIn: Boolean) {
        sharedPreferences.edit {
            putBoolean(PrefsKeys.IS_LOGGED_IN.name, isLoggedIn)
        }
    }
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(PrefsKeys.IS_LOGGED_IN.name, false)
    }

    fun saveFCMToken(token: String){
        sharedPreferences.edit {
            putString(PrefsKeys.FCM_TOKEN.name, token)
        }
    }
    fun getFCMToken(): String?{
        return sharedPreferences.getString(PrefsKeys.FCM_TOKEN.name, "")
    }

    fun saveEmailVerified(isVerified: Boolean){ // Otp Section
        sharedPreferences.edit {
            putBoolean(PrefsKeys.IS_EMAIL_VERIFIED.name, isVerified)
        }
    }
    fun isEmailVerified(): Boolean{
        return sharedPreferences.getBoolean(PrefsKeys.IS_EMAIL_VERIFIED.name, false)
    }

    fun saveRegistrationCode(code: String){
        sharedPreferences.edit {
            putString(PrefsKeys.REGISTRATION_CODE.name, code)
        }
    }
    fun getRegistrationCode(): String?{
        return sharedPreferences.getString(PrefsKeys.REGISTRATION_CODE.name, null)
    }

    fun saveCMEUrl(url: String){
        sharedPreferences.edit {
            putString(PrefsKeys.CME_URL.name, url)
        }
    }
    fun getCMEUrl(): String?{
        return sharedPreferences.getString(PrefsKeys.CME_URL.name, null)
    }

    fun saveVideoUrl(url: String){
        sharedPreferences.edit {
            putString(PrefsKeys.VIDEO_URL.name, url)
        }
    }
    fun getVideoUrl(): String?{
        return sharedPreferences.getString(PrefsKeys.VIDEO_URL.name, null)
    }

    fun saveWelcomeNotificationSent(isSent: Boolean){
        sharedPreferences.edit {
            putBoolean(PrefsKeys.IS_WELCOME_NOTIFICATION_SENT.name, isSent)
        }
    }
    fun isWelcomeNotificationSent(): Boolean{
        return sharedPreferences.getBoolean(PrefsKeys.IS_WELCOME_NOTIFICATION_SENT.name, false)
    }

    fun saveSplashViewed(isViewed: Boolean){
        sharedPreferences.edit {
            putBoolean(PrefsKeys.SPLASH_VIEWED.name, isViewed)
        }
    }
    fun isSplashViewed(): Boolean{
        return sharedPreferences.getBoolean(PrefsKeys.SPLASH_VIEWED.name,false)
    }

    fun saveLastLocationTime(millis: Long){
        sharedPreferences.edit {
            putLong(PrefsKeys.LOCATION_TIME.name, millis)
        }
    }
    fun getLastLocationTime(): Long{
        return sharedPreferences.getLong(PrefsKeys.LOCATION_TIME.name,0L)
    }
}

enum class PrefsKeys {
    USER_UID,
    IS_LOGGED_IN,
    FCM_TOKEN,
    IS_EMAIL_VERIFIED,
    CME_URL,
    VIDEO_URL,
    IS_WELCOME_NOTIFICATION_SENT,
    SPLASH_VIEWED,
    LOCATION_TIME,
    REGISTRATION_CODE
}