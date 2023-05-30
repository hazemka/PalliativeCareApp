package com.hazem.alkateb.palliativecare.api_notification

class ApiRepository(private val apiService: ApiService) {

    fun sendNotification(body: NotificationBody) = apiService.sendNotification(body)

    fun sendCustomNotification(body: NotificationBody) = apiService.sendCustomNotification(body)
}