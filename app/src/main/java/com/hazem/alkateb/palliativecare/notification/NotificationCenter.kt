package com.hazem.alkateb.palliativecare.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationCenter {

    companion object{
        private var notificationID = 100

         fun createNotification(context: Context,intent: Intent, channelId:String, contentTitle: String, contentText: String, smallIcon: Int): Notification {
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            return NotificationCompat.Builder(context, channelId)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setSmallIcon(smallIcon)
                .setAutoCancel(true)
                .build()
        }

         fun notifyNotification(context: Context,notification: Notification) {
            notificationID++
            val manger: NotificationManager
            if (Build.VERSION.SDK_INT >= 26) {
                manger = context.getSystemService(NotificationManager::class.java)!!
                manger.notify(notificationID, notification)
            } else {
                manger =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manger.notify(notificationID, notification)
            }
        }
    }

}