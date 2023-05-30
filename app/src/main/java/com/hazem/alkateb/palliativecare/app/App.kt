package com.hazem.alkateb.palliativecare.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.analytics.FirebaseAnalytics
import com.hazem.alkateb.palliativecare.R

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(applicationContext)
        val analytics = FirebaseAnalytics.getInstance(this)
    }
}

    private fun createNotificationChannel(context: Context){
        if (Build.VERSION.SDK_INT >= 26){
            val channel = NotificationChannel(
                context.resources.getString(R.string.NotificationChannelId),
                context.resources.getString(R.string.NotificationChannelName),
                NotificationManager.IMPORTANCE_HIGH)
            channel.setShowBadge(true)
            channel.enableVibration(true)
            val notificationManager: NotificationManager = getSystemService(context,NotificationManager::class.java)!!
            notificationManager.createNotificationChannel(channel)
        }
    }