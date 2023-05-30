package com.hazem.alkateb.palliativecare.doctor.ui.send_notification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazem.alkateb.palliativecare.api_notification.ApiClient
import com.hazem.alkateb.palliativecare.api_notification.ApiRepository
import com.hazem.alkateb.palliativecare.api_notification.NotificationBody
import com.hazem.alkateb.palliativecare.api_notification.TopicResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendNotificationViewModel:ViewModel() {

    val isNotificationSent = MutableLiveData<Boolean>()
    private val repository: ApiRepository = ApiRepository(ApiClient.apiService)

    fun sendNotification(body: NotificationBody){
        viewModelScope.launch (Dispatchers.IO){
            try {
                val call = repository.sendNotification(body)
                call.enqueue(object : Callback<TopicResponse> {
                    override fun onResponse(call: Call<TopicResponse>, response: Response<TopicResponse>) {
                        isNotificationSent.value = response.isSuccessful && response.code() == 200
                    }
                    override fun onFailure(call: Call<TopicResponse>, t: Throwable) {
                        isNotificationSent.value = false
                    }
                })
            }catch (e:Exception){
                isNotificationSent.value = false
            }
        }
    }

}