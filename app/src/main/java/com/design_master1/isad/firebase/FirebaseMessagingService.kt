package com.design_master1.isad.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.design_master1.isad.R
import com.design_master1.isad.ui.activities.MainActivity
import com.design_master1.isad.utils.helper.Helper
import com.design_master1.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMessagingService : FirebaseMessagingService() {

    @Inject lateinit var mPrefsController: PrefsController
    @Inject lateinit var mHelper: Helper

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        mPrefsController.saveFCMToken(p0)
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.data.get("title")
        val body = remoteMessage.data.get("message")

        pushNotification(title?:"", body?:"" )
    }

    private fun pushNotification(title: String, body: String){
        val intent = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val channelId = "swaac_elso_notifications_channel"
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this,channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setPriority(Notification.PRIORITY_MAX)
            .setVibrate(longArrayOf(0))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,"Channel human readable title",NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Channel description"
            channel.canShowBadge()
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(System.currentTimeMillis().toInt(),notificationBuilder.build())
    }
    companion object{
        private const val TAG = "FirebaseMessagingServic"
    }
}