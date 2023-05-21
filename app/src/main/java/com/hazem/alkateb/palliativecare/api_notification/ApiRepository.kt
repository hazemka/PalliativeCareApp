package com.hazem.alkateb.palliativecare.api_notification

class ApiRepository(private val apiService: ApiService) {

    fun sendCustomNotification(body: Body) = apiService.sendCustomNotification(body)

    fun sendTopicNotification(body: Body) = apiService.sendTopicNotification(body)
}