package com.design_master1.isad.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.design_master1.isad.databinding.ActivitySplashBinding
import com.design_master1.isad.model.view_models.activities.SplashActivityViewModel
import com.design_master1.isad.model.view_models.activities.SplashActivityViewModel.LoginState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySplashBinding
    private val mViewModel: SplashActivityViewModel by viewModels()
    private var isVideoCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(mBinding.root)


        FirebaseApp.initializeApp(this)

        mViewModel.loginState.observe(this){
            if (it != LoginState.LOGGING_IN && it != LoginState.IDLE) {
                    startActivity(Intent(this, MainActivity::class.java).putExtra("go_to", "go_to_dashboard"))
                    finish()
            }
        }


//        if (!mViewModel.mPrefsController.isSplashViewed()){
//            mBinding.videoView.setOnPreparedListener { mediaPlayer ->
//                val videoRatio = mediaPlayer.videoWidth / mediaPlayer.videoHeight.toFloat()
//                val screenRatio = mBinding.videoView.width / mBinding.videoView.height.toFloat()
//                val scaleX = videoRatio / screenRatio
//                if (scaleX >= 1f) {
//                    mBinding.videoView.scaleX = scaleX
//                } else {
//                    mBinding.videoView.scaleY = 1f / scaleX
//                }
//            }
//            val path = "android.resource://" + packageName + "/" + R.raw.splash_video
//            mBinding.videoView.setVideoURI(Uri.parse(path))
//
//            mBinding.videoView.setOnCompletionListener {
//                mViewModel.mPrefsController.saveSplashViewed(true)
//                isVideoCompleted = true
//                if (mViewModel.mPrefsController.isLoggedIn()) mViewModel.loginState.postValue(LoginState.LOGGED_IN)
//            }
//            mBinding.videoView.start()
//        }else{
//            Glide.with(this).load(R.drawable.splash_back).into(mBinding.imageView)
//        }

        if (!mViewModel.mPrefsController.isLoggedIn()) getFCMToken()

        Handler(Looper.getMainLooper()).postDelayed({
            if (mViewModel.mPrefsController.isLoggedIn()) mViewModel.loginState.postValue(LoginState.LOGGED_IN)
        }, 3000)
    }
    private fun getFCMToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener {
                if (it.isSuccessful){
                    mViewModel.mPrefsController.saveFCMToken(it.result)
                    mViewModel.login(getDeviceId_())
                }
            }.addOnFailureListener {
                Log.d(TAG, "getFCMToken: ${it.message}")
                it.printStackTrace()
            }
    }
    private fun getDeviceId_(): String{
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }
    companion object{
        private const val TAG = "SplashActivity"
    }
}