package com.design_master1.isad.utils.worker

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.design_master1.isad.service.LocationUpdateService


class MyWorker(val context: Context, params: WorkerParameters): Worker(context, params) {

    private val TAG = "MyWorker"
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        Log.d(TAG, "doWork: Done")
        Log.d(TAG, "onStartJob: STARTING JOB..")
        if (!isMyServiceRunning(LocationUpdateService::class.java)){
            val intent = Intent(context, LocationUpdateService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(intent)
            else context.startService(intent)
        }
        return Result.success()
    }
   private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}