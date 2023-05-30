package com.hazem.alkateb.palliativecare.api_notification


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

object ApiClient {

    private val BASE_URL = "https://fcm.googleapis.com/"
    const val key = ""

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService:ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

}

interface ApiService{
    @Headers(
        "Authorization: key=AAAAt-qgmrs:APA91bHPLC5yOlyR0Ay46z0zQ6iqTliSsO1krTileFqYAwpSqb6XEqCKwR6a2DGLXcNkI8cDMFJVvbRaoRDWjP3lGEOgS_soXNlmk7YEBvYq5iH5ifvvotVr0Tn9OTbyNRbkmuFbYUoD",
        "Content-Type:application/json")

    @POST("fcm/send")
    fun sendNotification(@Body notificationBody: NotificationBody):Call<TopicResponse>

    @Headers(
        "Authorization: key=AAAAt-qgmrs:APA91bHPLC5yOlyR0Ay46z0zQ6iqTliSsO1krTileFqYAwpSqb6XEqCKwR6a2DGLXcNkI8cDMFJVvbRaoRDWjP3lGEOgS_soXNlmk7YEBvYq5iH5ifvvotVr0Tn9OTbyNRbkmuFbYUoD",
        "Content-Type:application/json")
    @POST("fcm/send")
    fun sendCustomNotification(@Body notificationBody: NotificationBody):Call<CustomResponse>
}