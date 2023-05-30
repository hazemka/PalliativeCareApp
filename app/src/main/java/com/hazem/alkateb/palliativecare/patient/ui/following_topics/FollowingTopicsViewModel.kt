package com.hazem.alkateb.palliativecare.patient.ui.following_topics

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.hazem.alkateb.palliativecare.model.UserTopic

class FollowingTopicsViewModel : ViewModel() {

    val allTopics = MutableLiveData<ArrayList<UserTopic>>()
    var myTopicsArray = ArrayList<String>()
    val firestore = FirebaseFirestore.getInstance()

    private fun getAllTopics(){
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
                                if (myTopicsArray.contains(topic.id)){
                                    Log.e("hzm", "getAllTopics: ${topic.id}")
                                    data.add(UserTopic(topic.id,user.id,topic.getString("name")!!,topic.getString("description")!!
                                        ,topic.getString("imageUrl")!!,topic.get("numOfComments").toString().toInt()
                                        ,topic.get("numOfPosts").toString().toInt(),topic.get("numOfFollowers").toString().toInt()
                                        ,topic.getBoolean("show")!!))
                                }
                            }
                            allTopics.value = data
                        }
                        .addOnFailureListener {
                            allTopics.value  = ArrayList()
                        }
                }
            }
            .addOnFailureListener {
                allTopics.value  =ArrayList()
            }
    }

    fun getSubscriptionsIds(context: Context){
        val sharedPreferences =  context.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId","")!!)
            .collection("subscriptions")
            .get()
            .addOnSuccessListener {
                for (topic in it){
                    myTopicsArray.add(topic.getString("topicId")!!)
                }
                getAllTopics()
            }
            .addOnFailureListener {
                allTopics.value = ArrayList()
            }
    }
}