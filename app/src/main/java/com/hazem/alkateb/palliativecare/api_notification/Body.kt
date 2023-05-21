package com.hazem.alkateb.palliativecare.api_notification

data class Body(var to:String ,var notificationBody: NotificationBody)

data class NotificationBody(var body:String,var title:String)
