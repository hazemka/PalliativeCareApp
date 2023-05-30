package com.hazem.alkateb.palliativecare.patient.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.hazem.alkateb.palliativecare.model.Topic
import com.hazem.alkateb.palliativecare.model.UserTopic

class HomePatientViewModel : ViewModel() {

    val allTopics = MutableLiveData<ArrayList<Topic>>()
    val firestore = FirebaseFirestore.getInstance()

     fun getAllTopics(){
        val data = ArrayList<Topic>()
        firestore.collection("users")
            .whereEqualTo("userType","doctor")
            .get()
            .addOnSuccessListener{users->
                for (user in users){
                    FirebaseFirestore.getInstance().collection("users")
                        .document(user.id)
                        .collection("myTopics")
                        .whereEqualTo("show",true)
                        .limit(2)
                        .get()
                        .addOnSuccessListener {topics->
                            for ( topic in topics){
                                    data.add(
                                        Topic(topic.id,topic.getString("name")!!,topic.getString("description")!!
                                        ,topic.getString("imageUrl")!!,topic.get("numOfComments").toString().toInt()
                                        ,topic.get("numOfPosts").toString().toInt(),topic.get("numOfFollowers").toString().toInt()
                                        ,topic.getBoolean("show")!!)
                                    )

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

}