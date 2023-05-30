package com.hazem.alkateb.palliativecare.api_notification

data class NotificationBody(val to:String ,val notification: Data)

data class Data(val body:String,val title: String)
