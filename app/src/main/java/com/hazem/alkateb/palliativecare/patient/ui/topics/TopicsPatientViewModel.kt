package com.hazem.alkateb.palliativecare.patient.ui.topics

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.hazem.alkateb.palliativecare.model.SubscribeTopic
import com.hazem.alkateb.palliativecare.model.Topic
import com.hazem.alkateb.palliativecare.model.UserTopic

class TopicsPatientViewModel : ViewModel() {

    val allTopics = MutableLiveData<ArrayList<UserTopic>>()
    val firestore = FirebaseFirestore.getInstance()

    fun getAllTopics(){
        val data = ArrayList<UserTopic>()
        firestore.collection("users")
            .whereEqualTo("userType","doctor")
            .get()
            .addOnSuccessListener{users->
                for (user in users){
                    FirebaseFirestore.getInstance().collection("users")
                        .document(user.id)
                        .collection("myTopics")
                        .whereEqualTo("show",true)
                        .get()
                        .addOnSuccessListener {topics->
                            for ( topic in topics){
                                data.add(UserTopic(topic.id,user.id,topic.getString("name")!!,topic.getString("description")!!
                                        ,topic.getString("imageUrl")!!,topic.get("numOfComments").toString().toInt()
                                        ,topic.get("numOfPosts").toString().toInt(),topic.get("numOfFollowers").toString().toInt()
                                        ,topic.getBoolean("show")!!))
                            }
                            allTopics.value = data
                        }
                        .addOnFailureListener {
                            allTopics.value = ArrayList()
                        }
                }
            }
            .addOnFailureListener {
                allTopics.value = ArrayList()
            }
    }

    companion object{
        val isTopicSubscribed = MutableLiveData<Boolean>()
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

        fun updateNumOfFollowers(doctorId:String,topicId:String){
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
    }


    fun getSubscriptionsIds(context: Context){
        val sharedPreferences =  context.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId","")!!)
            .collection("subscriptions")
            .get()
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }
}