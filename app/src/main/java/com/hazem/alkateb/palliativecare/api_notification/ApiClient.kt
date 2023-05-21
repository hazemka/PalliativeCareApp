package com.hazem.alkateb.palliativecare.api_notification

import com.hazem.alkateb.palliativecare.api_notification.ApiClient.key
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

object ApiClient {

    private val BASE_URL = "https://fcm.googleapis.com"
    const val key = "key=AAAAt-qgmrs:APA91bHPLC5yOlyR0Ay46z0zQ6iqTliSsO1krTileFqYAwpSqb6XEqCKwR6a2DGLXcNkI8cDMFJVvbRaoRDWjP3lGEOgS_soXNlmk7YEBvYq5iH5ifvvotVr0Tn9OTbyNRbkmuFbYUoD"

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val apiService:ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

}

interface ApiService{
    @Headers(
        "Content-Type: application/json",
        "Authorization: $key")
    @POST
    fun sendCustomNotification(@Body body: com.hazem.alkateb.palliativecare.api_notification.Body):Call<CustomResponse>

    @Headers(
        "Content-Type: application/json",
        "Authorization: $key")
    @POST("/fcm/send/")
    fun sendTopicNotification(@Body body:com.hazem.alkateb.palliativecare.api_notification.Body):Call<TopicResponse>
}