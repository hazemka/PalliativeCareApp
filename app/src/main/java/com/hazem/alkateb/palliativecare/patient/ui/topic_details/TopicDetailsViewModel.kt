package com.hazem.alkateb.palliativecare.patient.ui.topic_details

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.hazem.alkateb.palliativecare.model.SubscribeTopic

class TopicDetailsViewModel : ViewModel() {

    val doctorDetails = MutableLiveData<ArrayList<String>>()
    var myTopicsArray = MutableLiveData<ArrayList<String>>()
    val isTopicSubscribed = MutableLiveData<Boolean>()
    val isTopicUnSubscribed = MutableLiveData<Boolean>()

    fun getDoctorDetails(doctorId:String){
        val data = ArrayList<String>()
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(doctorId)
            .get()
            .addOnSuccessListener {
                val name = "${it.getString("firstName")!!} ${it.getString("secondName")!!} ${it.getString("familyName")!!}"
                data.add(name)
                data.add(it.getString("photo")!!)
                doctorDetails.value = data
            }
            .addOnFailureListener {
                doctorDetails.value = ArrayList()
            }
    }

    fun getSubscriptionsIds(context: Context){
        val data = ArrayList<String>()
        val sharedPreferences =  context.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId","")!!)
            .collection("subscriptions")
            .get()
            .addOnSuccessListener {
                for (topic in it){
                    data.add(topic.getString("topicId")!!)
                }
                myTopicsArray.value = data
            }
            .addOnFailureListener {
                myTopicsArray.value = ArrayList()
            }
    }


    fun subscribeTopic(context: Context,subscribeTopic: SubscribeTopic){
        val sharedPreferences =  context.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseMessaging.getInstance().subscribeToTopic(subscribeTopic.topicId)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId","")!!)
            .collection("subscriptions")
            .add(subscribeTopic)
            .addOnSuccessListener {
                updateNumOfFollowers(subscribeTopic.doctorId, subscribeTopic.topicId)
            }
            .addOnFailureListener {
                isTopicSubscribed.value = false
            }
    }

    fun unSubscribeTopic(context: Context,subscribeTopic: SubscribeTopic){
        val sharedPreferences =  context.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseMessaging.getInstance().unsubscribeFromTopic(subscribeTopic.topicId)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId","")!!)
            .collection("subscriptions")
            .get()
            .addOnSuccessListener {
                for (i in it){
                    if(i.getString("topicId")!! == subscribeTopic.topicId){
                        FirebaseFirestore.getInstance().collection("users")
                            .document(sharedPreferences.getString("userId","")!!)
                            .collection("subscriptions")
                            .document(i.id)
                            .delete()
                            .addOnSuccessListener {
                                updateNumOfFollowersForUnSubscribe(subscribeTopic.doctorId,subscribeTopic.topicId)
                            }
                            .addOnFailureListener {
                                isTopicUnSubscribed.value = false
                            }
                    }
                }
            }
            .addOnFailureListener {
                isTopicUnSubscribed.value = false
            }
    }

    private fun updateNumOfFollowers(doctorId:String, topicId:String){
        FirebaseFirestore.getInstance().collection("users")
            .document(doctorId)
            .collection("myTopics")
            .document(topicId)
            .get()
            .addOnSuccessListener {
                val noOfFollowers = it.get("numOfFollowers").toString().toInt()
                FirebaseFirestore.getInstance().collection("users")
                    .document(doctorId)
                    .collection("myTopics")
                    .document(topicId)
                    .update("numOfFollowers",noOfFollowers+1)
                    .addOnSuccessListener {
                        isTopicSubscribed.value = true
                    }
                    .addOnFailureListener {
                        isTopicSubscribed.value = false
                    }
            }
            .addOnFailureListener {
                isTopicSubscribed.value = false
            }
    }

    private fun updateNumOfFollowersForUnSubscribe(doctorId:String, topicId:String){
        FirebaseFirestore.getInstance().collection("users")
            .document(doctorId)
            .collection("myTopics")
            .document(topicId)
            .get()
            .addOnSuccessListener {
                val noOfFollowers = it.get("numOfFollowers").toString().toInt()
                FirebaseFirestore.getInstance().collection("users")
                    .document(doctorId)
                    .collection("myTopics")
                    .document(topicId)
                    .update("numOfFollowers",noOfFollowers-1)
                    .addOnSuccessListener {
                        isTopicUnSubscribed.value = true
                    }
                    .addOnFailureListener {
                        isTopicUnSubscribed.value = false
                    }
            }
            .addOnFailureListener {
                isTopicUnSubscribed.value = false
            }
    }


}