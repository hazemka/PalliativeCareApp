package com.hazem.alkateb.palliativecare.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.SplashScreenActivity

class FCMService : FirebaseMessagingService(){

    private var notificationID = 100


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val body = message.notification!!.body.toString()
        val title = message.notification!!.title.toString()
        // This Notification to Notify it when The App is running...
        val notification = createNotification(
            Intent(this,SplashScreenActivity::class.java)
            ,getString(R.string.NotificationChannelId),title,body,R.drawable.ic_notification)
        // Notify Notification :
        notifyNotification(notification)

    }

    private fun createNotification(intent: Intent, channelId:String, contentTitle: String, contentText: String, smallIcon: Int): Notification {
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setContentIntent(pendingIntent)
            .setSmallIcon(smallIcon)
            .setAutoCancel(true)
            .build()
    }

    private fun notifyNotification(notification: Notification) {
        notificationID++
        val manger: NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            manger = applicationContext.getSystemService(NotificationManager::class.java)!!
            manger.notify(notificationID, notification)
        } else {
            manger =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manger.notify(notificationID, notification)
        }
    }
}